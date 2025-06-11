package ru.rakhmanov.myshop.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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

@SpringBootTest
@Import(ItemServiceImpl.class)
class ItemServiceImplIntegrationTest {

    @MockitoBean
    private ItemRepository itemRepository;

    @MockitoBean
    private OrderItemService orderItemService;

    @MockitoBean
    private ItemMapper itemMapper;

    @Autowired
    private ItemServiceImpl itemService;

    @Test
    void getItemsPageable_ShouldReturnItemsFromRepository() {
        String search = "test";
        SortTypeEnum sortType = SortTypeEnum.PRICE;
        int pageSize = 5;
        int pageNumber = 1;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "price"));
        List<Item> expectedItems = List.of(new Item());

        when(itemRepository.getItemsByNameLikeIgnoreCase("%" + search + "%", pageable)).thenReturn(expectedItems);

        List<Item> result = itemService.getItemsPageable(search, sortType, pageSize, pageNumber);

        assertEquals(expectedItems, result);
        verify(itemRepository).getItemsByNameLikeIgnoreCase("%" + search + "%", pageable);
    }

    @Test
    void getItemDtoById_ShouldCombineDataFromServices() {
        Long itemId = 1L;
        Long clientId = 1L;
        Item item = new Item();
        item.setId(itemId);
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(3);
        ItemDto expectedDto = new ItemDto();

        try (MockedStatic<RequestHeaderUtil> utilities = mockStatic(RequestHeaderUtil.class)) {
            utilities.when(RequestHeaderUtil::getClientId).thenReturn(clientId);

            when(orderItemService.getOrderItemByUserId(clientId)).thenReturn(List.of(orderItem));
            when(itemMapper.matToItemDto(item, 3)).thenReturn(expectedDto);

            ItemDto result = itemService.getItemDtoById(itemId);

            assertEquals(expectedDto, result);
            verify(orderItemService).getOrderItemByUserId(clientId);
            verify(itemMapper).matToItemDto(item, 3);
        }
    }

    @Test
    void getItemById_ShouldThrowExceptionWhenNotFound() {
        Long itemId = 999L;

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.getItemById(itemId));
        verify(itemRepository).findById(itemId);
    }
}