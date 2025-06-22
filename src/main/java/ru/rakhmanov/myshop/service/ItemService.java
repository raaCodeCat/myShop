package ru.rakhmanov.myshop.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.dto.SortTypeEnum;
import ru.rakhmanov.myshop.dto.db.entity.Item;
import ru.rakhmanov.myshop.dto.response.ItemDto;

public interface ItemService {

        Mono<ItemDto> getItemById(Long id);

        Flux<Item> getItemsPageable(String search, SortTypeEnum sortType, Integer pageSize, Integer pageNumber);

        Mono<Integer> getItemsCount(String search);

}
