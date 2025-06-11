package ru.rakhmanov.myshop.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rakhmanov.myshop.dto.entity.Order;
import ru.rakhmanov.myshop.dto.entity.OrderItem;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.mapper.OrderItemMapper;
import ru.rakhmanov.myshop.service.OrderItemService;
import ru.rakhmanov.myshop.service.OrderService;
import ru.rakhmanov.myshop.utils.RequestHeaderUtil;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private OrderService orderService;

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private OrderItemMapper orderItemMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void getCart_ShouldReturnMappedItemDtos() {
        Long clientId = 1L;
        Long orderId = 1L;
        Order order = new Order(clientId);
        order.setId(orderId);
        List<OrderItem> orderItems = List.of(new OrderItem());
        List<ItemDto> expectedDtos = List.of(new ItemDto());

        try (MockedStatic<RequestHeaderUtil> utilities = mockStatic(RequestHeaderUtil.class)) {
            utilities.when(RequestHeaderUtil::getClientId).thenReturn(clientId);

            when(orderService.getCurrentOrderByClientId(clientId)).thenReturn(order);
            when(orderItemService.getOrderItemsByOrderId(orderId)).thenReturn(orderItems);
            when(orderItemMapper.toItemDto(orderItems)).thenReturn(expectedDtos);

            List<ItemDto> result = cartService.getCart();

            assertEquals(expectedDtos, result);
            verify(orderService).getCurrentOrderByClientId(clientId);
            verify(orderItemService).getOrderItemsByOrderId(orderId);
            verify(orderItemMapper).toItemDto(orderItems);
        }
    }

    @Test
    void getTotal_ShouldCalculateCorrectSum() {
        ItemDto item1 = new ItemDto();
        item1.setPrice(BigDecimal.valueOf(100));
        item1.setCount(2);

        ItemDto item2 = new ItemDto();
        item2.setPrice(BigDecimal.valueOf(50));
        item2.setCount(3);

        List<ItemDto> items = List.of(item1, item2);

        BigDecimal result = cartService.getTotal(items);

        assertEquals(BigDecimal.valueOf(350), result);
    }

    @Test
    void getTotal_ShouldReturnZeroForEmptyList() {
        BigDecimal result = cartService.getTotal(List.of());

        assertEquals(BigDecimal.ZERO, result);
    }
}