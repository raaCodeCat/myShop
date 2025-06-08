package ru.rakhmanov.myshop.service;

import java.util.List;
import java.util.Map;

public interface OrderItemService {

    Map<Long, Integer> getItemsIdWithCountInCartByIds(List<Long> itemIds);

    void editItemInCurrentOrder(Long itemId, String action);

}
