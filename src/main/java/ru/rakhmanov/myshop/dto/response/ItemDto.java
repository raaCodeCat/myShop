package ru.rakhmanov.myshop.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemDto {

    private Long id;

    private String title;

    private String description;

    private BigDecimal price;

    private String imagePath;

    private Integer count = 0;

}
