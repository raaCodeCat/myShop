package ru.rakhmanov.myshop.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import ru.rakhmanov.myshop.dto.db.entity.OrderItem;

@DataR2dbcTest
@ActiveProfiles("test")
public class OrderItemRepositoryTest {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Test
    void findByOrderIdAndItemId_ShouldReturnOrderItem() {
        Long orderId = 1L;
        Long itemId = 1L;

        orderItemRepository.findByOrderIdAndItemId(orderId, itemId)
                .as(StepVerifier::create)
                .expectNextMatches(orderItem ->
                        orderItem.getOrderId().equals(orderId) &&
                                orderItem.getItemId().equals(itemId))
                .verifyComplete();
    }

    @Test
    void save_ShouldPersistOrderItem() {
        orderItemRepository.deleteById(1L);
        OrderItem newOrderItem = new OrderItem(1L, 2L);
        orderItemRepository.save(newOrderItem)
                .as(StepVerifier::create)
                .expectNextMatches(savedItem ->
                        savedItem.getId() != null &&
                                savedItem.getCount() == 1)
                .verifyComplete();
    }

    @Test
    void updateCount_ShouldModifyExistingOrderItem() {
        Long orderId = 1L;
        Long itemId = 1L;
        Integer newCount = 5;

        orderItemRepository.findByOrderIdAndItemId(orderId, itemId)
                .flatMap(orderItem -> {
                    orderItem.setCount(newCount);
                    return orderItemRepository.save(orderItem);
                })
                .as(StepVerifier::create)
                .expectNextMatches(updatedItem ->
                        updatedItem.getCount().equals(newCount))
                .verifyComplete();
    }
}