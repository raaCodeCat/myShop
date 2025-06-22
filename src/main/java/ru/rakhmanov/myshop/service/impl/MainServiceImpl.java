package ru.rakhmanov.myshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.rakhmanov.myshop.dto.SortTypeEnum;
import ru.rakhmanov.myshop.dto.db.entity.Item;
import ru.rakhmanov.myshop.dto.db.view.OrderDetails;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.dto.response.OrderDto;
import ru.rakhmanov.myshop.mapper.ItemMapper;
import ru.rakhmanov.myshop.repository.ItemRepository;
import ru.rakhmanov.myshop.service.ItemService;
import ru.rakhmanov.myshop.service.MainService;
import ru.rakhmanov.myshop.service.OrderItemService;
import ru.rakhmanov.myshop.service.OrderService;
import ru.rakhmanov.myshop.utils.RequestHeaderUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @Override
    public Flux<ItemDto> getItemsPageable(String search, SortTypeEnum sort, Integer pageSize, Integer pageNumber) {
        Long clientId = RequestHeaderUtil.getClientId();

        return orderService.getCurrentOrderByClientId(clientId)
                .flatMapMany(order ->
                        itemService.getItemsPageable(search, sort, pageSize, pageNumber)
                                .flatMap(item ->
                                        orderItemService.getCountForItemInOrder(item.getId(), order.getId())
                                                .defaultIfEmpty(0)
                                                .map(count -> itemMapper.mapToItemDto(item, count))
                                )
                                .switchIfEmpty(
                                        itemService.getItemsPageable(search, sort, pageSize, pageNumber)
                                                .map(item -> itemMapper.mapToItemDto(item, 0))
                                )
                );
    }

}
