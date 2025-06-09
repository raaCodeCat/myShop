package ru.rakhmanov.myshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long id;

    private String title;

    private String description;

    private BigDecimal price;

    private String imagePath;

    private Integer count = 0;

    public ItemDto(Long id, String title, BigDecimal price, Integer count) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.count = count;
    }
}
