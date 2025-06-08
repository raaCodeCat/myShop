package ru.rakhmanov.myshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rakhmanov.myshop.dto.entity.Order;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findOrderByUserIdAndIsPaidFalse(Long userId);

}
