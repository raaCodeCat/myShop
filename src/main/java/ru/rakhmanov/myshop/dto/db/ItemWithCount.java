package ru.rakhmanov.myshop.dto.db;

import lombok.Data;

@Data
public class ItemWithCount {

    private Long id;

    private String name;

    private String description;

    private Double price;

    private String imagePath;

    private Integer count;

}
