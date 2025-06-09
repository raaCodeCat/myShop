package ru.rakhmanov.myshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.rakhmanov.myshop.dto.SortTypeEnum;
import ru.rakhmanov.myshop.dto.entity.Item;
import ru.rakhmanov.myshop.dto.entity.Order;
import ru.rakhmanov.myshop.dto.entity.OrderItem;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.exeption.NotFoundException;
import ru.rakhmanov.myshop.mapper.ItemMapper;
import ru.rakhmanov.myshop.repository.ItemRepository;
import ru.rakhmanov.myshop.repository.OrderRepository;
import ru.rakhmanov.myshop.service.ItemService;
import ru.rakhmanov.myshop.service.OrderItemService;
import ru.rakhmanov.myshop.service.OrderService;
import ru.rakhmanov.myshop.utils.RequestHeaderUtil;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final OrderItemService orderItemService;
    private final ItemMapper itemMapper;

    @Override
    public List<Item> getItemsPageable(String search, SortTypeEnum sortType, Integer pageSize, Integer pageNumber) {

        Sort sort = switch (sortType) {
            case ALPHA -> Sort.by(Sort.Direction.ASC, "name");
            case PRICE -> Sort.by(Sort.Direction.ASC, "price");
            default -> Sort.unsorted();
        };

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        return itemRepository.getItemsByNameLikeIgnoreCase(makeSearchString(search), pageable);
    }

    @Override
    public Integer getItemsCount(String search) {
        return itemRepository.countItemsByNameLikeIgnoreCase(makeSearchString(search));
    }

    @Override
    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public ItemDto getItemDtoById(Long itemId) {
        Item item;
        Long clientId = RequestHeaderUtil.getClientId();
        List<OrderItem> orderItems = orderItemService.getOrderItemByUserId(clientId);

        item = orderItems.stream()
                .filter(oi -> itemId.equals(oi.getItem().getId()))
                .findFirst()
                .map(OrderItem::getItem)
                .orElse(this.getItemById(itemId));

        Integer count = orderItems.stream()
                .filter(oi -> itemId.equals(oi.getItem().getId()))
                .findFirst()
                .map(OrderItem::getCount)
                .orElse(0);

        return itemMapper.matToItemDto(item, count);
    }

    private String makeSearchString(String search) {
        return "%" + search + "%";
    }
}
