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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.service.ItemService;
import ru.rakhmanov.myshop.service.MainService;
import ru.rakhmanov.myshop.service.OrderItemService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(MainController.class)
@Import(MainController.class)
class MainControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private MainService mainService;

    @MockitoBean
    private ItemService itemService;

    @MockitoBean
    private OrderItemService orderItemService;

    @Test
    void getMainPage_ShouldReturnMainView() {
        when(mainService.getItemsPageable(anyString(), any(), anyInt(), anyInt()))
                .thenReturn(Flux.just(new ItemDto()));
        when(itemService.getItemsCount(anyString())).thenReturn(Mono.just(1));

        webTestClient.get()
                .uri("/main/items")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void editItemInCurrentOrder_ShouldRedirect_WhenActionIsPresent() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("action", "add");
        when(orderItemService.editItemInCurrentOrder(1L, "add")).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/main/items/1")
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/main/items");
    }

    @Test
    void editItemInCurrentOrder_ShouldReturnBadRequest_WhenActionIsMissing() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        webTestClient.post()
                .uri("/main/items/1")
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectStatus().isBadRequest();
    }
}