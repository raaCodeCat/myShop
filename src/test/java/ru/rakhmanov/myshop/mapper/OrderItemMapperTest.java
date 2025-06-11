package ru.rakhmanov.myshop.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.rakhmanov.myshop.config.MapperConfig;
import ru.rakhmanov.myshop.dto.entity.Item;
import ru.rakhmanov.myshop.dto.entity.Order;
import ru.rakhmanov.myshop.dto.entity.OrderItem;
import ru.rakhmanov.myshop.dto.response.ItemDto;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MapperConfig.class})
class OrderItemMapperTest {

    @Autowired
    private OrderItemMapper mapper;

    @Test
    void toItemDto_mapsSingleOrderItemCorrectly() {
        Item item = new Item();
        item.setId(10L);
        item.setName("Тестовый товар");
        item.setDescription("Описание товара");
        item.setPrice(new BigDecimal("999.99"));
        item.setImagePath("/images/test.jpg");

        OrderItem orderItem = new OrderItem(new Order(), item);
        orderItem.setCount(2);

        ItemDto result = mapper.toItemDto(orderItem);

        assertEquals(10L, result.getId());
        assertEquals("Тестовый товар", result.getTitle());
        assertEquals("Описание товара", result.getDescription());
        assertEquals(new BigDecimal("999.99"), result.getPrice());
        assertEquals("/images/test.jpg", result.getImagePath());
        assertEquals(2, result.getCount());
    }

    @Test
    void toItemDto_handlesNullOrderItem() {
        OrderItem orderItem = null;
        ItemDto result = mapper.toItemDto(orderItem);
        assertNull(result);
    }

    @Test
    void toItemDto_handlesNullOrderItemList() {
        List<OrderItem> orderItems = null;
        List<ItemDto> result = mapper.toItemDto(orderItems);
        assertNull(result);
    }

    @Test
    void toItemDto_mapsListOfOrderItems() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Товар 1");
        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Товар 2");

        OrderItem orderItem1 = new OrderItem(new Order(), item1);
        orderItem1.setCount(1);
        OrderItem orderItem2 = new OrderItem(new Order(), item2);
        orderItem2.setCount(3);

        List<ItemDto> results = mapper.toItemDto(List.of(orderItem1, orderItem2));

        assertEquals(2, results.size());
        assertEquals(1L, results.get(0).getId());
        assertEquals("Товар 1", results.get(0).getTitle());
        assertEquals(1, results.get(0).getCount());
        assertEquals(2L, results.get(1).getId());
        assertEquals("Товар 2", results.get(1).getTitle());
        assertEquals(3, results.get(1).getCount());
    }

    @Test
    void toItemDto_handlesNullList() {
        List<ItemDto> results = mapper.toItemDto((List<OrderItem>) null);
        assertNull(results);
    }

    @Test
    void toItemDto_handlesEmptyList() {
        List<ItemDto> results = mapper.toItemDto(List.of());
        assertTrue(results.isEmpty());
    }
}