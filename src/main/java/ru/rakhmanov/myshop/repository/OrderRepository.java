package ru.rakhmanov.myshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rakhmanov.myshop.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
