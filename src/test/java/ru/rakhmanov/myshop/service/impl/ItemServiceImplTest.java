package ru.rakhmanov.myshop.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.rakhmanov.myshop.dto.SortTypeEnum;
import ru.rakhmanov.myshop.dto.entity.Item;
import ru.rakhmanov.myshop.dto.entity.OrderItem;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.exeption.NotFoundException;
import ru.rakhmanov.myshop.mapper.ItemMapper;
import ru.rakhmanov.myshop.repository.ItemRepository;
import ru.rakhmanov.myshop.service.OrderItemService;
import ru.rakhmanov.myshop.utils.RequestHeaderUtil;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void getItemsPageable_ShouldReturnSortedItems() {
        String search = "test";
        SortTypeEnum sortType = SortTypeEnum.ALPHA;
        int pageSize = 10;
        int pageNumber = 0;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "name"));
        List<Item> expectedItems = List.of(new Item());

        when(itemRepository.getItemsByNameLikeIgnoreCase("%" + search + "%", pageable)).thenReturn(expectedItems);

        List<Item> result = itemService.getItemsPageable(search, sortType, pageSize, pageNumber);

        assertEquals(expectedItems, result);
        verify(itemRepository).getItemsByNameLikeIgnoreCase("%" + search + "%", pageable);
    }

    @Test
    void getItemsCount_ShouldReturnCorrectCount() {
        String search = "test";
        int expectedCount = 5;

        when(itemRepository.countItemsByNameLikeIgnoreCase("%" + search + "%")).thenReturn(expectedCount);

        Integer result = itemService.getItemsCount(search);

        assertEquals(expectedCount, result);
        verify(itemRepository).countItemsByNameLikeIgnoreCase("%" + search + "%");
    }

    @Test
    void getItemById_ShouldReturnItemWhenExists() {
        Long itemId = 1L;
        Item expectedItem = new Item();
        expectedItem.setId(itemId);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(expectedItem));

        Item result = itemService.getItemById(itemId);

        assertEquals(expectedItem, result);
        verify(itemRepository).findById(itemId);
    }

    @Test
    void getItemById_ShouldThrowNotFoundException() {
        Long itemId = 1L;

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.getItemById(itemId));
        verify(itemRepository).findById(itemId);
    }

    @Test
    void getItemDtoById_ShouldReturnDtoWithCountFromOrder() {
        Long itemId = 1L;
        Long clientId = 1L;
        Item item = new Item();
        item.setId(itemId);
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(2);
        ItemDto expectedDto = new ItemDto();

        try (MockedStatic<RequestHeaderUtil> utilities = mockStatic(RequestHeaderUtil.class)) {
            utilities.when(RequestHeaderUtil::getClientId).thenReturn(clientId);

            when(orderItemService.getOrderItemByUserId(clientId)).thenReturn(List.of(orderItem));
            when(itemMapper.matToItemDto(item, 2)).thenReturn(expectedDto);

            ItemDto result = itemService.getItemDtoById(itemId);

            assertEquals(expectedDto, result);
            verify(orderItemService).getOrderItemByUserId(clientId);
            verify(itemMapper).matToItemDto(item, 2);
        }
    }

    @Test
    void getItemDtoById_ShouldReturnDtoWithZeroCount() {
        Long itemId = 1L;
        Long clientId = 1L;
        Item item = new Item();
        item.setId(itemId);
        ItemDto expectedDto = new ItemDto();

        try (MockedStatic<RequestHeaderUtil> utilities = mockStatic(RequestHeaderUtil.class)) {
            utilities.when(RequestHeaderUtil::getClientId).thenReturn(clientId);

            when(orderItemService.getOrderItemByUserId(clientId)).thenReturn(List.of());
            when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
            when(itemMapper.matToItemDto(item, 0)).thenReturn(expectedDto);

            ItemDto result = itemService.getItemDtoById(itemId);

            assertEquals(expectedDto, result);
            verify(orderItemService).getOrderItemByUserId(clientId);
            verify(itemRepository).findById(itemId);
            verify(itemMapper).matToItemDto(item, 0);
        }
    }
}