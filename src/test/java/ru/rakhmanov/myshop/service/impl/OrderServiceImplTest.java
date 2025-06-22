package ru.rakhmanov.myshop.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.rakhmanov.myshop.dto.db.entity.Order;
import ru.rakhmanov.myshop.dto.db.view.OrderDetails;
import ru.rakhmanov.myshop.dto.response.OrderDto;
import ru.rakhmanov.myshop.repository.OrderDetailsDAO;
import ru.rakhmanov.myshop.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@SpringJUnitConfig
class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    @MockitoBean
    private OrderRepository orderRepository;

    @MockitoBean
    private OrderDetailsDAO orderDetailsDAO;

    @Test
    void getCurrentOrderByClientId_shouldReturnExistingOrder() {
        Long clientId = 999_999L;
        Order existingOrder = new Order(clientId);
        existingOrder.setId(1L);

        when(orderRepository.findByUserIdAndIsPaidFalse(clientId))
                .thenReturn(Mono.just(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.just(existingOrder));

        Mono<Order> result = orderService.getCurrentOrderByClientId(clientId);

        StepVerifier.create(result)
                .expectNextMatches(order ->
                        order.getId().equals(1L) &&
                                order.getUserId().equals(clientId))
                .verifyComplete();
    }

    @Test
    void getCurrentOrderByClientId_shouldCreateNewOrderWhenNotExists() {
        Long clientId = 999_999L;
        Order newOrder = new Order(clientId);
        newOrder.setId(2L);

        when(orderRepository.findByUserIdAndIsPaidFalse(clientId))
                .thenReturn(Mono.empty());
        when(orderRepository.save(any(Order.class)))
                .thenReturn(Mono.just(newOrder));

        Mono<Order> result = orderService.getCurrentOrderByClientId(clientId);

        StepVerifier.create(result)
                .expectNextMatches(order ->
                        order.getId().equals(2L) &&
                                order.getUserId().equals(clientId))
                .verifyComplete();
    }

    @Test
    void getOrderById_shouldReturnOrderDtoWhenExists() {
        Long orderId = 1L;
        OrderDetails detail = new OrderDetails();
        List<OrderDetails> details = List.of(detail);
        BigDecimal total = BigDecimal.TEN;

        when(orderDetailsDAO.existsByOrderId(orderId)).thenReturn(Mono.just(true));
        when(orderDetailsDAO.getOrderById(orderId)).thenReturn(Flux.fromIterable(details));
        when(orderDetailsDAO.getOrderTotalSum(orderId)).thenReturn(Mono.just(total));

        Mono<OrderDto> result = orderService.getOrderById(orderId);

        StepVerifier.create(result)
                .expectNextMatches(dto ->
                        dto.getItems().equals(details) &&
                                dto.getTotalSum().equals(total))
                .verifyComplete();
    }

    @Test
    void buyOrder_shouldMarkOrderAsPaid() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setIsPaid(false);

        when(orderRepository.findById(orderId)).thenReturn(Mono.just(order));
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.just(order));

        Mono<Void> result = orderService.buyOrder(orderId);

        StepVerifier.create(result)
                .verifyComplete();
    }
}