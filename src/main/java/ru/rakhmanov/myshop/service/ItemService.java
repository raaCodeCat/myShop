package ru.rakhmanov.myshop.service;

import ru.rakhmanov.myshop.dto.SortTypeEnum;
import ru.rakhmanov.myshop.dto.entity.Item;

import java.util.List;

public interface ItemService {

    List<Item> getItemsPageable(String search, SortTypeEnum sort, Integer pageSize, Integer pageNumber);

    Integer getItemsCount(String search);

}
