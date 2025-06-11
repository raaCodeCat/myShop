package ru.rakhmanov.myshop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.rakhmanov.myshop.dto.entity.Order;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private Order unpaidOrder;
    private Order paidOrder;

    @BeforeEach
    void setUp() {
        unpaidOrder = new Order(1L);
        orderRepository.save(unpaidOrder);

        paidOrder = new Order(2L);
        paidOrder.setIsPaid(true);
        orderRepository.save(paidOrder);
    }

    @Test
    void findOrderByUserIdAndIsPaidFalse_shouldFindUnpaidOrder() {
        Optional<Order> result = orderRepository.findOrderByUserIdAndIsPaidFalse(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getUserId());
        assertFalse(result.get().getIsPaid());
    }

    @Test
    void findOrderByUserIdAndIsPaidFalse_shouldNotFindPaidOrder() {
        Optional<Order> result = orderRepository.findOrderByUserIdAndIsPaidFalse(2L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findOrderByUserIdAndIsPaidFalse_shouldNotFindNonExistentOrder() {
        Optional<Order> result = orderRepository.findOrderByUserIdAndIsPaidFalse(999L);

        assertTrue(result.isEmpty());
    }

    @Test
    void saveOrder_shouldPersistAllFields() {
        Order newOrder = new Order(3L);
        Order saved = orderRepository.save(newOrder);

        Optional<Order> found = orderRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(3L, found.get().getUserId());
        assertFalse(found.get().getIsPaid());
    }
}