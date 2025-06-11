package ru.rakhmanov.myshop.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.rakhmanov.myshop.dto.entity.Order;
import ru.rakhmanov.myshop.dto.entity.OrderItem;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.mapper.OrderItemMapper;
import ru.rakhmanov.myshop.service.OrderItemService;
import ru.rakhmanov.myshop.service.OrderService;
import ru.rakhmanov.myshop.utils.RequestHeaderUtil;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class CartServiceImplIntegrationTest {

    @Autowired
    private CartServiceImpl cartService;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private OrderItemService orderItemService;

    @MockitoBean
    private OrderItemMapper orderItemMapper;

    @Test
    void getCart_ShouldReturnMappedItems() {
        Long clientId = 1L;
        Order order = new Order(clientId);
        order.setId(1L);
        OrderItem orderItem = new OrderItem();
        List<OrderItem> orderItems = List.of(orderItem);
        ItemDto itemDto = new ItemDto();
        List<ItemDto> expectedItems = List.of(itemDto);

        try (MockedStatic<RequestHeaderUtil> utilities = mockStatic(RequestHeaderUtil.class)) {
            utilities.when(RequestHeaderUtil::getClientId).thenReturn(clientId);

            when(orderService.getCurrentOrderByClientId(clientId)).thenReturn(order);
            when(orderItemService.getOrderItemsByOrderId(order.getId())).thenReturn(orderItems);
            when(orderItemMapper.toItemDto(orderItems)).thenReturn(expectedItems);

            List<ItemDto> result = cartService.getCart();

            assertEquals(expectedItems, result);
        }
    }

    @Test
    void getTotal_ShouldReturnCorrectSumWithRealImplementation() {
        ItemDto item1 = new ItemDto(1L, "Item1", BigDecimal.valueOf(150), 2);
        ItemDto item2 = new ItemDto(2L, "Item2", BigDecimal.valueOf(75), 4);
        List<ItemDto> items = List.of(item1, item2);

        BigDecimal result = cartService.getTotal(items);

        assertEquals(new BigDecimal("600"), result);
    }
}