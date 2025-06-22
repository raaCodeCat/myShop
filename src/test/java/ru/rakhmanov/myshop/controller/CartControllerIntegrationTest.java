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
import ru.rakhmanov.myshop.dto.response.OrderDto;
import ru.rakhmanov.myshop.service.OrderItemService;
import ru.rakhmanov.myshop.service.OrderService;

import java.util.Collections;

import static org.mockito.Mockito.when;

@WebFluxTest(CartController.class)
@Import(CartController.class)
class CartControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private OrderItemService orderItemService;

    @MockitoBean
    private OrderService orderService;

    @Test
    void cart_ShouldReturnCartView() {
        OrderDto order = new OrderDto();
        order.setItems(Collections.emptyList());
        when(orderService.getCart()).thenReturn(Mono.just(order));

        webTestClient.get()
                .uri("/cart/items")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void editItemInCurrentOrder_ShouldRedirect_WhenActionIsPresent() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("action", "increase");
        when(orderItemService.editItemInCurrentOrder(1L, "increase")).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/cart/items/1")
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/cart/items");
    }
}