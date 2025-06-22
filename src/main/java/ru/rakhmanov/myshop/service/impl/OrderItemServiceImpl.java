package ru.rakhmanov.myshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.dto.db.entity.OrderItem;
import ru.rakhmanov.myshop.repository.ItemRepository;
import ru.rakhmanov.myshop.repository.OrderItemRepository;
import ru.rakhmanov.myshop.service.OrderItemService;
import ru.rakhmanov.myshop.service.OrderService;
import ru.rakhmanov.myshop.utils.RequestHeaderUtil;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderService orderService;

    @Override
    public Mono<Void> editItemInCurrentOrder(Long itemId, String action) {
        Long clientId = RequestHeaderUtil.getClientId();

        return orderService.getCurrentOrderByClientId(clientId)
                .flatMap(order -> {
                    switch (action) {
                        case "plus" -> {
                            return incrementItemCountInOrder(itemId, order.getId());
                        }
                        case "minus" -> {
                            return decrementItemCountInOrder(itemId, order.getId());
                        }
                        case "delete" -> {
                            return deleteItemFromOrder(itemId, order.getId());
                        }
                        default -> {
                            return Mono.empty();
                        }
                    }
                });
    }

    private Mono<Void> incrementItemCountInOrder(Long itemId, Long orderId) {
        return orderItemRepository.findByOrderIdAndItemId(orderId, itemId)
                .flatMap(orderItem -> {
                    orderItem.setCount(orderItem.getCount() + 1);
                    return orderItemRepository.save(orderItem);
                })
                .switchIfEmpty(
                    orderItemRepository.save(new OrderItem(orderId, itemId)))
                .single()
                .then();
    }

    private Mono<Void> decrementItemCountInOrder(Long itemId, Long orderId) {
        return orderItemRepository.findByOrderIdAndItemId(orderId, itemId)
                .flatMap(orderItem -> {
                    if (orderItem.getCount() > 1) {
                        orderItem.setCount(orderItem.getCount() - 1);
                        return orderItemRepository.save(orderItem);
                    } else {
                        return orderItemRepository.delete(orderItem);
                    }
                })
                .then();
    }

    private Mono<Void> deleteItemFromOrder(Long itemId, Long orderId) {
        return orderItemRepository.findByOrderIdAndItemId(orderId, itemId)
                .flatMap(orderItemRepository::delete)
                .then();
    }

}
