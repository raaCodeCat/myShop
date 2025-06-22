package ru.rakhmanov.myshop.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.dto.db.entity.Order;
import ru.rakhmanov.myshop.dto.response.OrderDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Mono<Order> getCurrentOrderByClientId(Long clientId);

    Mono<OrderDto> getOrderById(Long orderId);

    Flux<OrderDto> getOrdersByUserId();

    Mono<OrderDto> getCart();

}
