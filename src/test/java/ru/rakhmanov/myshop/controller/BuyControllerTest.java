package ru.rakhmanov.myshop.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rakhmanov.myshop.service.OrderService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuyControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private BuyController buyController;

    @Test
    void buy_WhenOrderExists_ShouldRedirectToOrderPage() {
        Long orderId = 123L;
        when(orderService.getCurrentOrderId()).thenReturn(Optional.of(orderId));

        String result = buyController.buy();

        assertEquals("redirect:/orders/123?newOrder=true", result);
    }

    @Test
    void buy_WhenOrderDoesNotExist_ShouldReturnErrorPage() {
        when(orderService.getCurrentOrderId()).thenReturn(Optional.empty());

        String result = buyController.buy();

        assertEquals("error", result);
    }
}