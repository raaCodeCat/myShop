package ru.rakhmanov.myshop.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.rakhmanov.myshop.dto.db.entity.Order;
import ru.rakhmanov.myshop.dto.db.entity.OrderItem;
import ru.rakhmanov.myshop.repository.OrderItemRepository;
import ru.rakhmanov.myshop.service.OrderService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceImplTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    @Test
    void editItemInCurrentOrder_shouldIncrementItemCount() {
        Long itemId = 1L;
        Long clientId = 999_999L;
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        OrderItem orderItem = new OrderItem(orderId, itemId);
        orderItem.setCount(1);

        when(orderService.getCurrentOrderByClientId(clientId)).thenReturn(Mono.just(order));
        when(orderItemRepository.findByOrderIdAndItemId(orderId, itemId)).thenReturn(Mono.just(orderItem));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(Mono.just(orderItem));

        Mono<Void> result = orderItemService.editItemInCurrentOrder(itemId, "plus");

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void getCountForItemInOrder_shouldReturnCount() {
        Long itemId = 1L;
        Long orderId = 1L;
        Integer expectedCount = 2;
        OrderItem orderItem = new OrderItem();
        orderItem.setCount(expectedCount);

        when(orderItemRepository.findByOrderIdAndItemId(orderId, itemId)).thenReturn(Mono.just(orderItem));

        Mono<Integer> result = orderItemService.getCountForItemInOrder(itemId, orderId);

        StepVerifier.create(result)
                .expectNext(expectedCount)
                .verifyComplete();
    }
}