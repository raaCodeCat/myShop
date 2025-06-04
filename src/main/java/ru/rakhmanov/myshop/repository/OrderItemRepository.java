package ru.rakhmanov.myshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rakhmanov.myshop.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
