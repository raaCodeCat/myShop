package ru.rakhmanov.myshop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.dto.response.OrderDto;
import ru.rakhmanov.myshop.service.OrderService;

import static org.mockito.Mockito.when;

@WebFluxTest(OrderController.class)
@Import(OrderController.class)
class OrderControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private OrderService orderService;

    @Test
    void showOrder_ShouldReturnOrderView() {
        OrderDto order = new OrderDto();
        when(orderService.getOrderById(1L)).thenReturn(Mono.just(order));

        webTestClient.get()
                .uri("/orders/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void showOrder_ShouldProcessPurchase_ForNewOrder() {
        OrderDto order = new OrderDto();
        when(orderService.getOrderById(1L)).thenReturn(Mono.just(order));
        when(orderService.buyOrder(1L)).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/orders/1?newOrder=true")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void showAllOrders_ShouldReturnOrdersList() {
        OrderDto order = new OrderDto();
        when(orderService.getOrdersByUserId()).thenReturn(Flux.just(order));

        webTestClient.get()
                .uri("/orders")
                .exchange()
                .expectStatus().isOk();
    }
}