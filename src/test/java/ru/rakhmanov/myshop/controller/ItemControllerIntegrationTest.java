package ru.rakhmanov.myshop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.service.ItemService;
import ru.rakhmanov.myshop.service.OrderItemService;

import static org.mockito.Mockito.when;

@WebFluxTest(ItemController.class)
@Import(ItemController.class)
class ItemControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ItemService itemService;

    @MockitoBean
    private OrderItemService orderItemService;

    @Test
    void showItem_ShouldReturnItemView() {
        ItemDto item = new ItemDto();
        item.setId(1L);
        when(itemService.getItemById(1L)).thenReturn(Mono.just(item));

        webTestClient.get()
                .uri("/items/1")
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    void editItemInCurrentOrder_ShouldRedirect_WhenActionIsPresent() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("action", "add");
        when(orderItemService.editItemInCurrentOrder(1L, "add")).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/items/1")
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/items/1");
    }

    @Test
    void editItemInCurrentOrder_ShouldReturnBadRequest_WhenActionIsMissing() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        webTestClient.post()
                .uri("/items/1")
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectStatus().isBadRequest();
    }
}