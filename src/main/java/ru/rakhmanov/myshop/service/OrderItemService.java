package ru.rakhmanov.myshop.service;

import ru.rakhmanov.myshop.dto.entity.OrderItem;

import java.util.List;
import java.util.Map;

public interface OrderItemService {

    Map<Long, Integer> getItemsIdWithCountInCartByIds(List<Long> itemIds);

    void editItemInCurrentOrder(Long itemId, String action);

    List<OrderItem> getOrderItemsByOrderId(Long orderId);

}
