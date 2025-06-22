package ru.rakhmanov.myshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.exeption.NotFoundException;
import ru.rakhmanov.myshop.mapper.ItemMapper;
import ru.rakhmanov.myshop.repository.ItemRepository;
import ru.rakhmanov.myshop.service.ItemService;
import ru.rakhmanov.myshop.utils.RequestHeaderUtil;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public Mono<ItemDto> getItemById(Long id) {
        Long clientId = RequestHeaderUtil.getClientId();

        return itemRepository.findByItemIdWithCountInCart(id, clientId)
                .switchIfEmpty(Mono.error(new NotFoundException("Товар не найден")))
                .map(itemMapper::matToItemDto);
    }
}
