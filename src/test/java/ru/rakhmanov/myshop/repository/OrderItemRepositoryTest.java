package ru.rakhmanov.myshop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.rakhmanov.myshop.dto.entity.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class OrderItemRepositoryTest {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Order testOrder;
    private Item testItem1;
    private Item testItem2;
    private OrderItem orderItem1;
    private OrderItem orderItem2;

    @BeforeEach
    void setUp() {
        testOrder = new Order(1L);
        orderRepository.save(testOrder);

        testItem1 = new Item();
        testItem1.setName("Test Item 1");
        testItem1.setDescription("Description 1");
        testItem1.setPrice(BigDecimal.valueOf(100));
        testItem1.setImagePath("image1.jpg");
        itemRepository.save(testItem1);

        testItem2 = new Item();
        testItem2.setName("Test Item 2");
        testItem2.setDescription("Description 2");
        testItem2.setPrice(BigDecimal.valueOf(200));
        testItem2.setImagePath("image2.jpg");
        itemRepository.save(testItem2);

        orderItem1 = new OrderItem(testOrder, testItem1);
        orderItem1.setCount(2);
        orderItemRepository.save(orderItem1);

        orderItem2 = new OrderItem(testOrder, testItem2);
        orderItem2.setCount(1);
        orderItemRepository.save(orderItem2);
    }

    @Test
    void getOrderItemByOrderIdAndItemIdIn_shouldReturnMatchingItems() {
        List<OrderItem> result = orderItemRepository.getOrderItemByOrderIdAndItemIdIn(
                testOrder.getId(),
                List.of(testItem1.getId(), testItem2.getId())
        );

        assertEquals(2, result.size());
        assertTrue(result.contains(orderItem1));
        assertTrue(result.contains(orderItem2));
    }

    @Test
    void getOrderItemByOrderIdAndItemId_shouldReturnSpecificItem() {
        Optional<OrderItem> result = orderItemRepository.getOrderItemByOrderIdAndItemId(
                testOrder.getId(),
                testItem1.getId()
        );

        assertTrue(result.isPresent());
        assertEquals(orderItem1.getId(), result.get().getId());
        assertEquals(2, result.get().getCount());
    }

    @Test
    void getOrderItemByOrderId_shouldReturnAllOrderItems() {
        List<OrderItem> result = orderItemRepository.getOrderItemByOrderId(testOrder.getId());

        assertEquals(2, result.size());
        assertEquals(testOrder.getId(), result.get(0).getOrder().getId());
    }

    @Test
    void getOrderItemByUserId_shouldReturnUnpaidOrderItems() {
        List<OrderItem> result = orderItemRepository.getOrderItemByUserId(testOrder.getUserId());

        assertEquals(2, result.size());
        assertFalse(result.get(0).getOrder().getIsPaid());
        assertEquals(testOrder.getUserId(), result.get(0).getOrder().getUserId());
    }

    @Test
    void getOrderItemsByOrderIdFull_shouldFetchItemData() {
        List<OrderItem> result = orderItemRepository.getOrderItemsByOrderIdFull(testOrder.getId());

        assertEquals(2, result.size());
        assertNotNull(result.get(0).getItem().getName());
        assertNotNull(result.get(0).getItem().getDescription());
    }

    @Test
    void getOrderItemsByClientIdFull_shouldReturnAllClientItems() {
        List<OrderItem> result = orderItemRepository.getOrderItemsByClientIdFull(testOrder.getUserId());

        assertEquals(2, result.size());
        assertEquals(testOrder.getUserId(), result.get(0).getOrder().getUserId());
        assertNotNull(result.get(0).getItem().getPrice());
    }
}