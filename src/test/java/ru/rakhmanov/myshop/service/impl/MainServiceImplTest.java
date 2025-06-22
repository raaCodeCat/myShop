package ru.rakhmanov.myshop.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.rakhmanov.myshop.dto.SortTypeEnum;
import ru.rakhmanov.myshop.dto.db.entity.Item;
import ru.rakhmanov.myshop.dto.db.entity.Order;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.mapper.ItemMapper;
import ru.rakhmanov.myshop.service.ItemService;
import ru.rakhmanov.myshop.service.OrderItemService;
import ru.rakhmanov.myshop.service.OrderService;

import static org.mockito.Mockito.when;

@SpringBootTest
@SpringJUnitConfig
class MainServiceImplTest {

    @Autowired
    private MainServiceImpl mainService;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private ItemService itemService;

    @MockitoBean
    private OrderItemService orderItemService;

    @MockitoBean
    private ItemMapper itemMapper;

    @Test
    void getItemsPageable_shouldReturnItemsWithCounts() {
        String search = "test";
        SortTypeEnum sort = SortTypeEnum.ALPHA;
        Integer pageSize = 10;
        Integer pageNumber = 0;
        Long clientId = 999_999L;
        Long orderId = 1L;
        Item item = new Item();
        item.setId(1L);
        Order order = new Order();
        order.setId(orderId);
        ItemDto itemDto = new ItemDto();

        when(orderService.getCurrentOrderByClientId(clientId)).thenReturn(Mono.just(order));
        when(itemService.getItemsPageable(search, sort, pageSize, pageNumber)).thenReturn(Flux.just(item));
        when(orderItemService.getCountForItemInOrder(item.getId(), orderId)).thenReturn(Mono.just(1));
        when(itemMapper.mapToItemDto(item, 1)).thenReturn(itemDto);

        Flux<ItemDto> result = mainService.getItemsPageable(search, sort, pageSize, pageNumber);

        StepVerifier.create(result)
                .expectNext(itemDto)
                .verifyComplete();
    }
}