package ru.rakhmanov.myshop.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class OrderDto {

    private Long id;

    private List<ItemDto> items;

    private BigDecimal totalSum;

}
