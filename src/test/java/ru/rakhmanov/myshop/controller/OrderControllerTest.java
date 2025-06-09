package ru.rakhmanov.myshop.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import ru.rakhmanov.myshop.dto.response.OrderDto;
import ru.rakhmanov.myshop.service.OrderService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerUnitTest {

    @Mock
    private OrderService orderService;

    @Mock
    private Model model;

    @InjectMocks
    private OrderController orderController;

    @Test
    void showOrder_ShouldReturnOrderViewWithAttributes() {
        Long orderId = 1L;
        OrderDto mockOrder = new OrderDto();
        when(orderService.getOrderById(orderId)).thenReturn(mockOrder);

        String viewName = orderController.showOrder(orderId, false, model);

        assertEquals("order", viewName);
        verify(orderService).buyOrder(orderId);
        verify(orderService).getOrderById(orderId);
        verify(model).addAttribute("order", mockOrder);
        verify(model).addAttribute("newOrder", false);
    }

    @Test
    void showAllOrders_ShouldReturnOrdersViewWithAttributes() {
        List<OrderDto> mockOrders = Collections.singletonList(new OrderDto());
        when(orderService.getOrdersByClient()).thenReturn(mockOrders);

        String viewName = orderController.showAllOrders(model);

        assertEquals("orders", viewName);
        verify(orderService).getOrdersByClient();
        verify(model).addAttribute("orders", mockOrders);
    }
}