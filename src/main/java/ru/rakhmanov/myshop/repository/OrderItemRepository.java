package ru.rakhmanov.myshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rakhmanov.myshop.dto.entity.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> getOrderItemByOrderIdAndItemIdIn(Long orderId, List<Long> itemIds);

    Optional<OrderItem> getOrderItemByOrderIdAndItemId(Long orderId, Long itemId);

    List<OrderItem> getOrderItemByOrderId(Long orderId);

}
