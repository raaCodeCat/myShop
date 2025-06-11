package ru.rakhmanov.myshop.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rakhmanov.myshop.dto.entity.Order;
import ru.rakhmanov.myshop.exeption.NotFoundException;
import ru.rakhmanov.myshop.mapper.OrderMapper;
import ru.rakhmanov.myshop.repository.OrderItemRepository;
import ru.rakhmanov.myshop.repository.OrderRepository;
import ru.rakhmanov.myshop.utils.RequestHeaderUtil;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void getCurrentOrderByClientId_ShouldCreateNewOrderWhenNotExists() {
        Long clientId = 1L;
        Order newOrder = new Order(clientId);

        when(orderRepository.findOrderByUserIdAndIsPaidFalse(clientId)).thenReturn(Optional.empty());
        when(orderRepository.save(any(Order.class))).thenReturn(newOrder);

        Order result = orderService.getCurrentOrderByClientId(clientId);

        assertEquals(newOrder, result);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void getCurrentOrderId_ShouldReturnOrderIdWhenExists() {
        Long clientId = 1L;
        Long orderId = 1L;
        Order order = new Order(clientId);
        order.setId(orderId);

        try (MockedStatic<RequestHeaderUtil> utilities = mockStatic(RequestHeaderUtil.class)) {
            utilities.when(RequestHeaderUtil::getClientId).thenReturn(clientId);

            when(orderRepository.findOrderByUserIdAndIsPaidFalse(clientId)).thenReturn(Optional.of(order));

            Optional<Long> result = orderService.getCurrentOrderId();

            assertTrue(result.isPresent());
            assertEquals(orderId, result.get());
        }
    }

    @Test
    void getOrderById_ShouldThrowNotFoundExceptionWhenOrderItemsEmpty() {
        Long orderId = 1L;

        when(orderItemRepository.getOrderItemsByOrderIdFull(orderId)).thenReturn(List.of());

        assertThrows(NotFoundException.class, () -> orderService.getOrderById(orderId));
    }

    @Test
    void buyOrder_ShouldSetOrderAsPaid() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.buyOrder(orderId);

        assertTrue(order.getIsPaid());
        verify(orderRepository).save(order);
    }
}