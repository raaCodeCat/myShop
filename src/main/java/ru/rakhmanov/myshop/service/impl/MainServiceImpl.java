package ru.rakhmanov.myshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rakhmanov.myshop.dto.SortTypeEnum;
import ru.rakhmanov.myshop.dto.entity.Item;
import ru.rakhmanov.myshop.dto.entity.OrderItem;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.mapper.ItemMapper;
import ru.rakhmanov.myshop.service.ItemService;
import ru.rakhmanov.myshop.service.MainService;
import ru.rakhmanov.myshop.service.OrderItemService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    private final ItemService itemService;
    private final OrderItemService orderItemService;
    private final ItemMapper itemMapper;

    @Override
    public List<ItemDto> getItemsPageable(String search, SortTypeEnum sort, Integer pageSize, Integer pageNumber) {

        List<Item> items = itemService.getItemsPageable(search, sort, pageSize, pageNumber);

        List<Long> itemIds = items.stream()
                .map(Item::getId)
                .toList();

        Map<Long, Integer> orderItems = orderItemService.getItemsIdWithCountInCartByIds(itemIds);

        return items.stream()
                .map(item -> {
                    Integer count = Optional.ofNullable(orderItems.get(item.getId())).orElse(0);
                    return itemMapper.matToItemDto(item, count);
                })
                .toList();

    }
}
