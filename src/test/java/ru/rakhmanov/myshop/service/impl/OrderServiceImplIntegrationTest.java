package ru.rakhmanov.myshop.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.rakhmanov.myshop.dto.entity.Order;
import ru.rakhmanov.myshop.dto.entity.OrderItem;
import ru.rakhmanov.myshop.dto.response.OrderDto;
import ru.rakhmanov.myshop.exeption.NotFoundException;
import ru.rakhmanov.myshop.mapper.OrderMapper;
import ru.rakhmanov.myshop.repository.OrderItemRepository;
import ru.rakhmanov.myshop.repository.OrderRepository;
import ru.rakhmanov.myshop.utils.RequestHeaderUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(OrderServiceImpl.class)
class OrderServiceImplIntegrationTest {

    @MockitoBean
    private OrderRepository orderRepository;

    @MockitoBean
    private OrderItemRepository orderItemRepository;

    @MockitoBean
    private OrderMapper orderMapper;

    @Autowired
    private OrderServiceImpl orderService;

    @Test
    void getOrdersByClient_ShouldReturnMappedOrderDtos() {
        Long clientId = 1L;
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        OrderDto expectedDto = new OrderDto();

        try (MockedStatic<RequestHeaderUtil> utilities = mockStatic(RequestHeaderUtil.class)) {
            utilities.when(RequestHeaderUtil::getClientId).thenReturn(clientId);

            when(orderItemRepository.getOrderItemsByClientIdFull(clientId)).thenReturn(List.of(orderItem));
            when(orderMapper.mapToOrderDto(orderId, List.of(orderItem))).thenReturn(expectedDto);

            List<OrderDto> result = orderService.getOrdersByClient();

            assertEquals(1, result.size());
            assertEquals(expectedDto, result.get(0));
        }
    }

    @Test
    void getOrderById_ShouldMapOrderItemsToDto() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        OrderDto expectedDto = new OrderDto();

        when(orderItemRepository.getOrderItemsByOrderIdFull(orderId)).thenReturn(List.of(orderItem));
        when(orderMapper.mapToOrderDto(orderId, List.of(orderItem))).thenReturn(expectedDto);

        OrderDto result = orderService.getOrderById(orderId);

        assertEquals(expectedDto, result);
    }

    @Test
    void buyOrder_ShouldThrowNotFoundExceptionWhenOrderNotExists() {
        Long orderId = 999L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderService.buyOrder(orderId));
    }
}