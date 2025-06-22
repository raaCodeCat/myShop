package ru.rakhmanov.myshop.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;
import ru.rakhmanov.myshop.dto.db.entity.Order;

@DataR2dbcTest
@ActiveProfiles("test")
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void findByUserIdAndIsPaidFalse_ShouldReturnUnpaidOrder() {
        Long userId = 1L;

        orderRepository.findByUserIdAndIsPaidFalse(userId)
                .as(StepVerifier::create)
                .expectNextMatches(order ->
                        order.getUserId().equals(userId) &&
                                !order.getIsPaid())
                .verifyComplete();
    }

    @Test
    void save_ShouldPersistNewOrder() {
        Long userId = 2L;
        Order newOrder = new Order(userId);

        orderRepository.save(newOrder)
                .as(StepVerifier::create)
                .expectNextMatches(savedOrder ->
                        savedOrder.getId() != null &&
                                savedOrder.getUserId().equals(userId) &&
                                !savedOrder.getIsPaid())
                .verifyComplete();
    }

    @Test
    void update_ShouldModifyExistingOrder() {
        Long orderId = 1L;
        Boolean newStatus = true;

        orderRepository.findById(orderId)
                .flatMap(order -> {
                    order.setIsPaid(newStatus);
                    return orderRepository.save(order);
                })
                .as(StepVerifier::create)
                .expectNextMatches(updatedOrder ->
                        updatedOrder.getIsPaid().equals(newStatus))
                .verifyComplete();
    }
}