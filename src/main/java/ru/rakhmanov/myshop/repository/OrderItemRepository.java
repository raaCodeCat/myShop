package ru.rakhmanov.myshop.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.dto.db.entity.OrderItem;


public interface OrderItemRepository extends R2dbcRepository<OrderItem, Long> {

    Mono<OrderItem> findByOrderIdAndItemId(Long orderId, Long itemId);

}
