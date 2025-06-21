package ru.rakhmanov.myshop.dto.db.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails {

    private Long orderId;

    private Long itemId;

    private String title;

    private String description;

    private BigDecimal price;

    private Integer count;

    private BigDecimal total;

    private Boolean isPaid;

    private Long userId;

    private String imagePath;

}
