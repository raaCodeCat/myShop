package ru.rakhmanov.myshop.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.dto.db.entity.Order;

public interface OrderRepository extends R2dbcRepository<Order, Long> {

    Mono<Order> findByUserIdAndIsPaidFalse(Long userId);

}
