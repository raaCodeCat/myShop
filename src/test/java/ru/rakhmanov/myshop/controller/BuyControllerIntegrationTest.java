package ru.rakhmanov.myshop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.service.OrderService;

import static org.mockito.Mockito.when;

@WebFluxTest(BuyController.class)
class BuyControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private OrderService orderService;

    @Test
    void buy_ShouldReturnRedirect_WhenOrderExists() {
        Long orderId = 456L;
        when(orderService.getCurrentOrderId()).thenReturn(Mono.just(orderId));

        webTestClient.post()
                .uri("/buy")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/orders/456?newOrder=true");
    }

}