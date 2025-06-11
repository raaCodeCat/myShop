package ru.rakhmanov.myshop.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rakhmanov.myshop.dto.SortTypeEnum;
import ru.rakhmanov.myshop.dto.entity.Item;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.mapper.ItemMapper;
import ru.rakhmanov.myshop.service.ItemService;
import ru.rakhmanov.myshop.service.OrderItemService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainServiceImplTest {

    @Mock
    private ItemService itemService;

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private MainServiceImpl mainService;

    @Test
    void getItemsPageable_ShouldReturnMappedItemsWithCounts() {
        String search = "test";
        SortTypeEnum sort = SortTypeEnum.ALPHA;
        int pageSize = 10;
        int pageNumber = 0;

        Item item1 = new Item();
        item1.setId(1L);
        Item item2 = new Item();
        item2.setId(2L);
        List<Item> items = List.of(item1, item2);

        Map<Long, Integer> orderItems = Map.of(1L, 2, 2L, 0);

        ItemDto dto1 = new ItemDto();
        ItemDto dto2 = new ItemDto();
        List<ItemDto> expectedDtos = List.of(dto1, dto2);

        when(itemService.getItemsPageable(search, sort, pageSize, pageNumber)).thenReturn(items);
        when(orderItemService.getItemsIdWithCountInCartByIds(List.of(1L, 2L))).thenReturn(orderItems);
        when(itemMapper.matToItemDto(item1, 2)).thenReturn(dto1);
        when(itemMapper.matToItemDto(item2, 0)).thenReturn(dto2);

        List<ItemDto> result = mainService.getItemsPageable(search, sort, pageSize, pageNumber);

        assertEquals(expectedDtos, result);
        verify(itemService).getItemsPageable(search, sort, pageSize, pageNumber);
        verify(orderItemService).getItemsIdWithCountInCartByIds(List.of(1L, 2L));
        verify(itemMapper).matToItemDto(item1, 2);
        verify(itemMapper).matToItemDto(item2, 0);
    }

    @Test
    void getItemsPageable_ShouldHandleEmptyItemList() {
        String search = "empty";
        SortTypeEnum sort = SortTypeEnum.PRICE;
        int pageSize = 5;
        int pageNumber = 1;

        when(itemService.getItemsPageable(search, sort, pageSize, pageNumber)).thenReturn(List.of());

        List<ItemDto> result = mainService.getItemsPageable(search, sort, pageSize, pageNumber);

        assertEquals(0, result.size());
        verify(itemService).getItemsPageable(search, sort, pageSize, pageNumber);
    }
}
