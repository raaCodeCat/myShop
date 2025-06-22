package ru.rakhmanov.myshop.dto.db.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("orderitems")
public class OrderItem {
    @Id
    private Long id;

    @Column("order_id")
    private Long orderId;

    @Column("item_id")
    private Long itemId;

    private Integer count;

    public OrderItem(Long orderId, Long itemId) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.count = 1;
    }

}
