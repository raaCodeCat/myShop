package ru.rakhmanov.myshop.dto.response;

import lombok.Data;
import ru.rakhmanov.myshop.dto.db.view.OrderDetails;

import java.math.BigDecimal;
import java.util.List;


@Data
public class OrderDto {

    private Long id;

    private List<OrderDetails> items;

    private BigDecimal totalSum;

}
