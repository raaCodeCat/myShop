package ru.rakhmanov.myshop.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.rakhmanov.myshop.config.MapperConfig;
import ru.rakhmanov.myshop.dto.entity.Item;
import ru.rakhmanov.myshop.dto.entity.OrderItem;
import ru.rakhmanov.myshop.dto.response.OrderDto;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MapperConfig.class})
class OrderMapperTest {

    @Autowired
    private OrderMapper mapper;

    @Test
    void mapToOrderDto_correctlyMapsIdAndItems() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setPrice(new BigDecimal("100.00"));
        Item item2 = new Item();
        item2.setId(2L);
        item2.setPrice(new BigDecimal("200.00"));

        OrderItem orderItem1 = new OrderItem(null, item1);
        orderItem1.setCount(2);
        OrderItem orderItem2 = new OrderItem(null, item2);
        orderItem2.setCount(1);

        OrderDto result = mapper.mapToOrderDto(5L, List.of(orderItem1, orderItem2));

        assertEquals(5L, result.getId());
        assertEquals(2, result.getItems().size());
        assertEquals(new BigDecimal("400.00"), result.getTotalSum());
    }

    @Test
    void mapToOrderDto_calculatesCorrectTotalSum() {
        Item item = new Item();
        item.setId(1L);
        item.setPrice(new BigDecimal("50.50"));

        OrderItem orderItem1 = new OrderItem(null, item);
        orderItem1.setCount(3);
        OrderItem orderItem2 = new OrderItem(null, item);
        orderItem2.setCount(2);

        OrderDto result = mapper.mapToOrderDto(1L, List.of(orderItem1, orderItem2));

        assertEquals(new BigDecimal("252.50"), result.getTotalSum());
    }

    @Test
    void mapToOrderDto_handlesEmptyItemsList() {
        OrderDto result = mapper.mapToOrderDto(1L, List.of());

        assertEquals(1L, result.getId());
        assertTrue(result.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, result.getTotalSum());
    }

    @Test
    void mapToOrderDto_handlesNullItemsList() {
        OrderDto result = mapper.mapToOrderDto(1L, null);

        assertEquals(1L, result.getId());
        assertNull(result.getItems());
        assertEquals(BigDecimal.ZERO,result.getTotalSum());
    }

}