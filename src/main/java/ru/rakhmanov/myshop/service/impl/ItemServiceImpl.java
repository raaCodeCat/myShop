package ru.rakhmanov.myshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.dto.SortTypeEnum;
import ru.rakhmanov.myshop.dto.db.entity.Item;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.exeption.NotFoundException;
import ru.rakhmanov.myshop.mapper.ItemMapper;
import ru.rakhmanov.myshop.repository.ItemRepository;
import ru.rakhmanov.myshop.service.ItemService;
import ru.rakhmanov.myshop.service.OrderItemService;
import ru.rakhmanov.myshop.utils.RequestHeaderUtil;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final OrderItemService orderItemService;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public Mono<ItemDto> getItemById(Long id) {
        Long clientId = RequestHeaderUtil.getClientId();

        return itemRepository.findByItemIdWithCountInCart(id, clientId)
                .switchIfEmpty(Mono.error(new NotFoundException("Товар не найден")))
                .map(itemMapper::mapToItemDto);
    }

    @Override
    public Flux<Item> getItemsPageable(String search, SortTypeEnum sortType, Integer pageSize, Integer pageNumber) {
        Sort sort = switch (sortType) {
            case ALPHA -> Sort.by(Sort.Direction.ASC, "name");
            case PRICE -> Sort.by(Sort.Direction.ASC, "price");
            default -> Sort.unsorted();
        };

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        return itemRepository.findByNameLikeIgnoreCase(makeSearchString(search), pageable);

    }

    @Override
    public Mono<Integer> getItemsCount(String search) {
        return itemRepository.countByNameLikeIgnoreCase(makeSearchString(search));
    }

    private String makeSearchString(String search) {
        return "%" + search + "%";
    }
}
