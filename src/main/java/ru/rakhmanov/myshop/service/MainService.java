package ru.rakhmanov.myshop.service;

import reactor.core.publisher.Flux;
import ru.rakhmanov.myshop.dto.SortTypeEnum;
import ru.rakhmanov.myshop.dto.response.ItemDto;

public interface MainService {

    Flux<ItemDto> getItemsPageable(String search, SortTypeEnum sort, Integer pageSize, Integer pageNumber);

}
