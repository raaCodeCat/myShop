package ru.rakhmanov.myshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.rakhmanov.myshop.dto.entity.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> getOrderItemByOrderIdAndItemIdIn(Long orderId, List<Long> itemIds);

    Optional<OrderItem> getOrderItemByOrderIdAndItemId(Long orderId, Long itemId);

    List<OrderItem> getOrderItemByOrderId(Long orderId);

    @Query("select oi " +
            "from OrderItem oi " +
            "inner join oi.order o " +
            "inner join fetch oi.item i " +
            "where " +
            "o.userId = :userId " +
            "and o.isPaid = false ")
    List<OrderItem> getOrderItemByUserId(@Param("userId") Long userId);

    @Query("select oi " +
            "from OrderItem oi " +
            "inner join oi.order o " +
            "inner join fetch oi.item i " +
            "where " +
            "o.id = :orderId ")
    List<OrderItem> getOrderItemsByOrderIdFull(@Param("orderId") Long orderId);

    @Query("select oi " +
            "from OrderItem oi " +
            "inner join oi.order o " +
            "inner join fetch oi.item i " +
            "where " +
            "o.userId = :userId ")
    List<OrderItem> getOrderItemsByClientIdFull(@Param("userId") Long clientId);

}
