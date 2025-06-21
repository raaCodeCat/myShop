package ru.rakhmanov.myshop.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.dto.db.view.OrderDetails;

import java.math.BigDecimal;

public interface OrderDetailsDAO {

    Flux<OrderDetails> getOrderById(Long orderId);

    Flux<OrderDetails> getAllOrdersByUserId(Long userId);

    Mono<Boolean> existsByOrderId(Long orderId);

    Mono<BigDecimal> getOrderTotalSum(Long orderId);

}
