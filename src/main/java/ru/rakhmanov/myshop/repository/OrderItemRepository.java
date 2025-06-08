package ru.rakhmanov.myshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.rakhmanov.myshop.dto.entity.OrderItem;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> getOrderItemByOrderIdAndItemIdIn(Long orderId, List<Long> itemIds);

}
