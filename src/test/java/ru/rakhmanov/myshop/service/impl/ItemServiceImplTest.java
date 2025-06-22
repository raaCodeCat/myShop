package ru.rakhmanov.myshop.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.rakhmanov.myshop.dto.SortTypeEnum;
import ru.rakhmanov.myshop.dto.db.ItemWithCount;
import ru.rakhmanov.myshop.dto.db.entity.Item;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.exeption.NotFoundException;
import ru.rakhmanov.myshop.mapper.ItemMapper;
import ru.rakhmanov.myshop.repository.ItemRepository;
import ru.rakhmanov.myshop.utils.RequestHeaderUtil;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void getItemById_shouldReturnItemDtoWhenItemExists() {
        Long itemId = 1L;
        Long clientId = 999_999L;
        ItemWithCount itemWithCount = new ItemWithCount();
        ItemDto expectedDto = new ItemDto();

        try (MockedStatic<RequestHeaderUtil> utilities = org.mockito.Mockito.mockStatic(RequestHeaderUtil.class)) {
            utilities.when(RequestHeaderUtil::getClientId).thenReturn(clientId);

            when(itemRepository.findByItemIdWithCountInCart(itemId, clientId)).thenReturn(Mono.just(itemWithCount));
            when(itemMapper.mapToItemDto(itemWithCount)).thenReturn(expectedDto);

            Mono<ItemDto> result = itemService.getItemById(itemId);

            StepVerifier.create(result)
                    .expectNext(expectedDto)
                    .verifyComplete();
        }
    }

    @Test
    void getItemById_shouldThrowNotFoundExceptionWhenItemNotExists() {
        Long itemId = 1L;
        Long clientId = 999_999L;

        try (MockedStatic<RequestHeaderUtil> utilities = org.mockito.Mockito.mockStatic(RequestHeaderUtil.class)) {
            utilities.when(RequestHeaderUtil::getClientId).thenReturn(clientId);

            when(itemRepository.findByItemIdWithCountInCart(itemId, clientId)).thenReturn(Mono.empty());

            Mono<ItemDto> result = itemService.getItemById(itemId);

            StepVerifier.create(result)
                    .expectError(NotFoundException.class)
                    .verify();
        }
    }

    @Test
    void getItemsPageable_shouldReturnFluxOfItems() {
        String search = "test";
        SortTypeEnum sortType = SortTypeEnum.ALPHA;
        Integer pageSize = 10;
        Integer pageNumber = 0;
        Item item = new Item();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "name"));

        when(itemRepository.findByNameLikeIgnoreCase("%" + search + "%", pageable)).thenReturn(Flux.just(item));

        Flux<Item> result = itemService.getItemsPageable(search, sortType, pageSize, pageNumber);

        StepVerifier.create(result)
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    void getItemsCount_shouldReturnCount() {
        String search = "test";
        Integer expectedCount = 5;

        when(itemRepository.countByNameLikeIgnoreCase("%" + search + "%")).thenReturn(Mono.just(expectedCount));

        Mono<Integer> result = itemService.getItemsCount(search);

        StepVerifier.create(result)
                .expectNext(expectedCount)
                .verifyComplete();
    }
}