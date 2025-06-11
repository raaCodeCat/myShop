package ru.rakhmanov.myshop.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rakhmanov.myshop.dto.entity.Item;
import ru.rakhmanov.myshop.dto.entity.Order;
import ru.rakhmanov.myshop.dto.entity.OrderItem;
import ru.rakhmanov.myshop.repository.ItemRepository;
import ru.rakhmanov.myshop.repository.OrderItemRepository;
import ru.rakhmanov.myshop.service.OrderService;
import ru.rakhmanov.myshop.utils.RequestHeaderUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceImplTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    @Test
    void getItemsIdWithCountInCartByIds_ShouldReturnItemCountsMap() {
        Long clientId = 1L;
        Long orderId = 1L;
        List<Long> itemIds = List.of(1L, 2L);
        Order order = new Order();
        order.setId(orderId);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setCount(2);
        Item item1 = new Item();
        item1.setId(1L);
        orderItem1.setItem(item1);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setCount(3);
        Item item2 = new Item();
        item2.setId(2L);
        orderItem2.setItem(item2);

        try (MockedStatic<RequestHeaderUtil> utilities = mockStatic(RequestHeaderUtil.class)) {
            utilities.when(RequestHeaderUtil::getClientId).thenReturn(clientId);

            when(orderService.getCurrentOrderByClientId(clientId)).thenReturn(order);
            when(orderItemRepository.getOrderItemByOrderIdAndItemIdIn(orderId, itemIds))
                    .thenReturn(List.of(orderItem1, orderItem2));

            Map<Long, Integer> result = orderItemService.getItemsIdWithCountInCartByIds(itemIds);

            assertEquals(2, result.size());
            assertEquals(2, result.get(1L));
            assertEquals(3, result.get(2L));
        }
    }

    @Test
    void editItemInCurrentOrder_ShouldIncrementItemCount() {
        Long clientId = 1L;
        Long orderId = 1L;
        Long itemId = 1L;
        Order order = new Order();
        order.setId(orderId);
        OrderItem orderItem = new OrderItem();
        orderItem.setCount(2);

        try (MockedStatic<RequestHeaderUtil> utilities = mockStatic(RequestHeaderUtil.class)) {
            utilities.when(RequestHeaderUtil::getClientId).thenReturn(clientId);

            when(orderService.getCurrentOrderByClientId(clientId)).thenReturn(order);
            when(orderItemRepository.getOrderItemByOrderIdAndItemId(orderId, itemId))
                    .thenReturn(Optional.of(orderItem));

            orderItemService.editItemInCurrentOrder(itemId, "plus");

            verify(orderItemRepository).save(any(OrderItem.class));
        }
    }

    @Test
    void getOrderItemsByOrderId_ShouldReturnOrderItems() {
        Long orderId = 1L;
        List<OrderItem> expectedItems = List.of(new OrderItem());

        when(orderItemRepository.getOrderItemByOrderId(orderId)).thenReturn(expectedItems);

        List<OrderItem> result = orderItemService.getOrderItemsByOrderId(orderId);

        assertEquals(expectedItems, result);
    }
}