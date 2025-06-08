package ru.rakhmanov.myshop.service;

import ru.rakhmanov.myshop.dto.SortTypeEnum;
import ru.rakhmanov.myshop.dto.response.ItemDto;

import java.util.List;

public interface MainService {

    List<ItemDto> getItemsPageable(String search, SortTypeEnum sort, Integer pageSize, Integer pageNumber);

}
