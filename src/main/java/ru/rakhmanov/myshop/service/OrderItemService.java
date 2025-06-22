package ru.rakhmanov.myshop.service;

import reactor.core.publisher.Mono;

public interface OrderItemService {

    Mono<Void> editItemInCurrentOrder(Long itemId, String action);

    Mono<Integer> getCountForItemInOrder(Long itemId, Long orderId);

}
