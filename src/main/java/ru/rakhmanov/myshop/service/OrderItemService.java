package ru.rakhmanov.myshop.service;

import ru.rakhmanov.myshop.dto.entity.OrderItem;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderItemService {

    Map<Long, Integer> getItemsIdWithCountInCartByIds(List<Long> itemIds);

    void editItemInCurrentOrder(Long itemId, String action);

    List<OrderItem> getOrderItemsByOrderId(Long orderId);

    List<OrderItem> getOrderItemByUserId(Long userId);

}
