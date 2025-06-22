package ru.rakhmanov.myshop.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.dto.db.entity.Order;
import ru.rakhmanov.myshop.dto.response.OrderDto;

public interface OrderService {

    Mono<Order> getCurrentOrderByClientId(Long clientId);

    Mono<OrderDto> getOrderById(Long orderId);

    Flux<OrderDto> getOrdersByUserId();

    Mono<OrderDto> getCart();

    Mono<Long> getCurrentOrderId();

}
