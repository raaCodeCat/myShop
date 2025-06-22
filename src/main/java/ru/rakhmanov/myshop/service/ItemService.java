package ru.rakhmanov.myshop.service;

import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.dto.response.ItemDto;

public interface ItemService {

        Mono<ItemDto> getItemById(Long id);

}
