package ru.rakhmanov.myshop.repository.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.rakhmanov.myshop.dto.db.view.OrderDetails;

import java.math.BigDecimal;

@SpringBootTest
@ActiveProfiles("test")
class OrderDetailsDAOImplTest {

    @Autowired
    private OrderDetailsDAOImpl orderDetailsDAO;

    @Test
    void getOrderById_shouldReturnOrderDetails() {
        Long orderId = 1L;

        orderDetailsDAO.getOrderById(orderId)
                .as(StepVerifier::create)
                .expectNextMatches(details ->
                        details.getOrderId().equals(orderId)
                )
                .verifyComplete();
    }

    @Test
    void getAllOrdersByUserId_shouldReturnOrderDetails() {
        Long userId = 1L;

        orderDetailsDAO.getAllOrdersByUserId(userId)
                .as(StepVerifier::create)
                .expectNextMatches(details ->
                        details.getUserId().equals(userId)
                )
                .verifyComplete();
    }

    @Test
    void existsByOrderId_shouldReturnBoolean() {
        Long orderId = 1L;

        orderDetailsDAO.existsByOrderId(orderId)
                .as(StepVerifier::create)
                .expectNextMatches(exists -> exists)
                .verifyComplete();
    }

    @Test
    void getOrderTotalSum_shouldReturnSum() {
        Long orderId = 1L;

        orderDetailsDAO.getOrderTotalSum(orderId)
                .as(StepVerifier::create)
                .expectNextMatches(sum -> sum.compareTo(BigDecimal.ZERO) > 0)
                .verifyComplete();
    }
}