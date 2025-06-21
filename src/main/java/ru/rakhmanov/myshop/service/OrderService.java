package ru.rakhmanov.myshop.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.dto.response.OrderDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {

//    Order getCurrentOrderByClientId(Long clientId);
//
//    Optional<Long> getCurrentOrderId();
//
//    OrderDto getOrderById(Long id);
//
//    void buyOrder(Long orderId);
//
//    List<OrderDto> getOrdersByClient();

    Mono<OrderDto> getOrderById(Long orderId);

    Flux<OrderDto> getOrdersByUserId();

}
