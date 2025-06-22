package ru.rakhmanov.myshop.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.rakhmanov.myshop.dto.response.OrderDto;
import ru.rakhmanov.myshop.service.OrderService;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerUnitTest {

    @Mock
    private OrderService orderService;

    @Mock
    private Model model;

    @InjectMocks
    private OrderController orderController;

    @Test
    void showOrder_ShouldReturnRendering_ForExistingOrder() {
        OrderDto order = new OrderDto();
        when(orderService.getOrderById(1L)).thenReturn(Mono.just(order));

        Mono<Rendering> result = orderController.showOrder(1L, false);

        StepVerifier.create(result)
                .expectNextMatches(rendering ->
                        "order".equals(rendering.view()) &&
                                rendering.modelAttributes().get("order") == order &&
                                rendering.modelAttributes().get("newOrder").equals(false)
                )
                .verifyComplete();
    }

    @Test
    void showOrder_ShouldProcessNewOrder_WhenFlagIsTrue() {
        OrderDto order = new OrderDto();
        when(orderService.getOrderById(1L)).thenReturn(Mono.just(order));
        when(orderService.buyOrder(1L)).thenReturn(Mono.empty());

        Mono<Rendering> result = orderController.showOrder(1L, true);

        StepVerifier.create(result)
                .expectNextMatches(rendering ->
                        rendering.modelAttributes().get("newOrder").equals(true)
                )
                .verifyComplete();

        verify(orderService).buyOrder(1L);
    }

    @Test
    void showAllOrders_ShouldReturnViewWithOrders() {
        List<OrderDto> orders = List.of(new OrderDto());
        when(orderService.getOrdersByUserId()).thenReturn(Flux.fromIterable(orders));

        Mono<String> result = orderController.showAllOrders(model);

        StepVerifier.create(result)
                .expectNext("orders")
                .verifyComplete();

        verify(model).addAttribute("orders", orders);
    }
}