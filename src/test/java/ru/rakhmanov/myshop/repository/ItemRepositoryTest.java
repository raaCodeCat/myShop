package ru.rakhmanov.myshop.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

@DataR2dbcTest
@ActiveProfiles("test")
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private DatabaseClient databaseClient;

    @Test
    void findByItemIdWithCountInCart_ShouldReturnItemWithCount() {
        Long itemId = 1L;
        Long userId = 1L;

        itemRepository.findByItemIdWithCountInCart(itemId, userId)
                .as(StepVerifier::create)
                .expectNextMatches(item ->
                        item.getId().equals(itemId) &&
                                item.getCount() == 2)
                .verifyComplete();
    }

    @Test
    void findByNameLikeIgnoreCase_ShouldReturnFilteredItems() {
        String search = "гаджет";
        PageRequest page = PageRequest.of(0, 5);

        itemRepository.findByNameLikeIgnoreCase("%" + search + "%", page)
                .as(StepVerifier::create)
                .expectNextMatches(item ->
                        item.getName().toLowerCase().contains(search.toLowerCase()))
                .verifyComplete();
    }

    @Test
    void countByNameLikeIgnoreCase_ShouldReturnCorrectCount() {
        String search = "тестовый";

        itemRepository.countByNameLikeIgnoreCase("%" + search + "%")
                .as(StepVerifier::create)
                .expectNext(2)
                .verifyComplete();
    }
}