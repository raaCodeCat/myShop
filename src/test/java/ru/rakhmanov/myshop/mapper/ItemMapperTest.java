package ru.rakhmanov.myshop.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.rakhmanov.myshop.config.MapperConfig;
import ru.rakhmanov.myshop.dto.entity.Item;
import ru.rakhmanov.myshop.dto.entity.OrderItem;
import ru.rakhmanov.myshop.dto.entity.Order;
import ru.rakhmanov.myshop.dto.response.ItemDto;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MapperConfig.class})
class ItemMapperTest {

    @Autowired
    private ItemMapper itemMapper;

    @Test
    void matToItemDtoFromItemAndCount() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setPrice(new BigDecimal("10.99"));
        item.setImagePath("/images/test.jpg");

        ItemDto result = itemMapper.matToItemDto(item, 5);

        assertEquals(1L, result.getId());
        assertEquals("Test Item", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals(new BigDecimal("10.99"), result.getPrice());
        assertEquals("/images/test.jpg", result.getImagePath());
        assertEquals(5, result.getCount());
    }

    @Test
    void matToItemDtoFromOrderItem() {
        Item item = new Item();
        item.setId(2L);
        item.setName("Order Item");
        item.setDescription("Order Description");
        item.setPrice(new BigDecimal("20.50"));
        item.setImagePath("/images/order.jpg");

        Order order = new Order();
        OrderItem orderItem = new OrderItem(order, item);
        orderItem.setCount(3);

        ItemDto result = itemMapper.matToItemDto(orderItem);

        assertEquals(2L, result.getId());
        assertEquals("Order Item", result.getTitle());
        assertEquals("Order Description", result.getDescription());
        assertEquals(new BigDecimal("20.50"), result.getPrice());
        assertEquals("/images/order.jpg", result.getImagePath());
        assertEquals(3, result.getCount());
    }

    @Test
    void matToItemDtoListFromOrderItems() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item 1");
        item1.setPrice(new BigDecimal("10.00"));

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item 2");
        item2.setPrice(new BigDecimal("20.00"));

        Order order = new Order();
        OrderItem orderItem1 = new OrderItem(order, item1);
        orderItem1.setCount(1);
        OrderItem orderItem2 = new OrderItem(order, item2);
        orderItem2.setCount(2);

        List<ItemDto> results = itemMapper.matToItemDto(List.of(orderItem1, orderItem2));

        assertEquals(2, results.size());
        assertEquals(1L, results.get(0).getId());
        assertEquals("Item 1", results.get(0).getTitle());
        assertEquals(1, results.get(0).getCount());
        assertEquals(2L, results.get(1).getId());
        assertEquals("Item 2", results.get(1).getTitle());
        assertEquals(2, results.get(1).getCount());
    }

    @Test
    void matToItemDtoWithNullInput() {
        assertNull(itemMapper.matToItemDto((Item) null, null));
        assertNull(itemMapper.matToItemDto((OrderItem) null));
        assertNull(itemMapper.matToItemDto((List<OrderItem>) null));
    }
}