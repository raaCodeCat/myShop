package ru.rakhmanov.myshop.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.rakhmanov.myshop.service.OrderService;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuyControllerUnitTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private BuyController buyController;

    @Test
    void buy_ShouldRedirectToOrderPage_WhenOrderExists() {
        Long orderId = 123L;
        when(orderService.getCurrentOrderId()).thenReturn(Mono.just(orderId));

        Mono<String> result = buyController.buy();

        StepVerifier.create(result)
                .expectNext("redirect:/orders/123?newOrder=true")
                .verifyComplete();
    }

    @Test
    void buy_ShouldReturnError_WhenOrderDoesNotExist() {
        when(orderService.getCurrentOrderId()).thenReturn(Mono.empty());

        Mono<String> result = buyController.buy();

        StepVerifier.create(result)
                .expectNext("error")
                .verifyComplete();
    }
}