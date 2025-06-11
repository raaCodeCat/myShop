package ru.rakhmanov.myshop.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.rakhmanov.myshop.dto.entity.Item;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void shouldFindItemsByNameLikeIgnoreCase() {
        Item item1 = new Item();
        item1.setName("Test Product");
        item1.setDescription("Description");
        item1.setPrice(BigDecimal.valueOf(100.50));
        item1.setImagePath("path1.jpg");
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("Another Product");
        item2.setDescription("Description");
        item2.setPrice(BigDecimal.valueOf(200.00));
        item2.setImagePath("path2.jpg");
        itemRepository.save(item2);

        PageRequest pageable = PageRequest.of(0, 10);
        List<Item> foundItems = itemRepository.getItemsByNameLikeIgnoreCase("%product%", pageable);

        assertEquals(2, foundItems.size());
        assertTrue(foundItems.stream().anyMatch(i -> i.getName().equals("Test Product")));
        assertTrue(foundItems.stream().anyMatch(i -> i.getName().equals("Another Product")));
    }

    @Test
    void shouldReturnEmptyListWhenNoMatchesFound() {
        Item item = new Item();
        item.setName("Unique Item");
        item.setDescription("Description");
        item.setPrice(BigDecimal.valueOf(300.00));
        item.setImagePath("path3.jpg");
        itemRepository.save(item);

        PageRequest pageable = PageRequest.of(0, 10);
        List<Item> foundItems = itemRepository.getItemsByNameLikeIgnoreCase("%nonexistent%", pageable);

        assertTrue(foundItems.isEmpty());
    }

    @Test
    void shouldCountItemsByNameLikeIgnoreCase() {
        Item item1 = new Item();
        item1.setName("Count Test 1");
        item1.setDescription("Description");
        item1.setPrice(BigDecimal.valueOf(150.00));
        item1.setImagePath("path4.jpg");
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("Count Test 2");
        item2.setDescription("Description");
        item2.setPrice(BigDecimal.valueOf(250.00));
        item2.setImagePath("path5.jpg");
        itemRepository.save(item2);

        int count = itemRepository.countItemsByNameLikeIgnoreCase("%count test%");

        assertEquals(2, count);
    }

    @Test
    void shouldReturnZeroWhenNoMatchesForCount() {
        Item item = new Item();
        item.setName("Single Item");
        item.setDescription("Description");
        item.setPrice(BigDecimal.valueOf(350.00));
        item.setImagePath("path6.jpg");
        itemRepository.save(item);

        int count = itemRepository.countItemsByNameLikeIgnoreCase("%notfound%");

        assertEquals(0, count);
    }

    @Test
    void shouldFindItemsWithPagination() {
        for (int i = 1; i <= 15; i++) {
            Item item = new Item();
            item.setName("Paged Item " + i);
            item.setDescription("Description " + i);
            item.setPrice(BigDecimal.valueOf(100 + i));
            item.setImagePath("path" + i + ".jpg");
            itemRepository.save(item);
        }

        PageRequest firstPage = PageRequest.of(0, 5);
        List<Item> firstPageItems = itemRepository.getItemsByNameLikeIgnoreCase("%paged%", firstPage);

        PageRequest secondPage = PageRequest.of(1, 5);
        List<Item> secondPageItems = itemRepository.getItemsByNameLikeIgnoreCase("%paged%", secondPage);

        assertEquals(5, firstPageItems.size());
        assertEquals(5, secondPageItems.size());
        assertNotEquals(firstPageItems.get(0).getName(), secondPageItems.get(0).getName());
    }
}