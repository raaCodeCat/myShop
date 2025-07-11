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
@Table("orders")
public class Order {
    @Id
    private Long id;

    @Column("user_id")
    private Long userId;

    @Column("is_paid")
    private Boolean isPaid;

    public Order(Long userId) {
        this.userId = userId;
        this.isPaid = false;
    }
}
