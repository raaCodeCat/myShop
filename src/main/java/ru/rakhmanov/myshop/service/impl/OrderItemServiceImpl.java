package ru.rakhmanov.myshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rakhmanov.myshop.dto.entity.Item;
import ru.rakhmanov.myshop.dto.entity.Order;
import ru.rakhmanov.myshop.dto.entity.OrderItem;
import ru.rakhmanov.myshop.exeption.NotFoundException;
import ru.rakhmanov.myshop.repository.ItemRepository;
import ru.rakhmanov.myshop.repository.OrderItemRepository;
import ru.rakhmanov.myshop.repository.OrderRepository;
import ru.rakhmanov.myshop.service.OrderItemService;
import ru.rakhmanov.myshop.service.OrderService;
import ru.rakhmanov.myshop.utils.RequestHeaderUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;
    private final OrderService orderService;

    @Override
    public Map<Long, Integer> getItemsIdWithCountInCartByIds(List<Long> itemIds) {
        Long clientId = RequestHeaderUtil.getClientId();
        Order order = orderService.getCurrentOrderByClientId(clientId);
        List<OrderItem> orderItems = orderItemRepository.getOrderItemByOrderIdAndItemIdIn(order.getId(), itemIds);

        return orderItems.stream()
                .collect(Collectors.toMap(
                        orderItem -> orderItem.getItem().getId(),
                        OrderItem::getCount,
                        (existing, replacement) -> replacement
                ));
    }

    @Override
    public void editItemInCurrentOrder(Long itemId, String action) {
        Long clientId = RequestHeaderUtil.getClientId();
        Order order = orderService.getCurrentOrderByClientId(clientId);

        switch (action) {
            case "plus" -> incrementItemCountInOrder(itemId, order);
            case "minus" -> decrementItemCountInOrder(itemId, order);
            case "delete" -> deleteItemFromOrder(itemId, order);
        }
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.getOrderItemByOrderId(orderId);
    }

    private void incrementItemCountInOrder(Long itemId, Order order) {
        Optional<OrderItem> optionalOrderItem = orderItemRepository.getOrderItemByOrderIdAndItemId(order.getId(), itemId);
        OrderItem orderItem;

        if (optionalOrderItem.isPresent()) {
            orderItem = optionalOrderItem.get();
            orderItem.setCount(orderItem.getCount() + 1);
        } else {
            Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Товар не найден"));
            orderItem = new OrderItem(order, item);
        }

        orderItemRepository.save(orderItem);
    }

    @Override
    public List<OrderItem> getOrderItemByUserId(Long userId) {
        return orderItemRepository.getOrderItemByUserId(userId);
    }

    private void decrementItemCountInOrder(Long itemId, Order order) {
        Optional<OrderItem> optionalOrderItem = orderItemRepository.getOrderItemByOrderIdAndItemId(order.getId(), itemId);

        if (optionalOrderItem.isPresent()) {
            OrderItem orderItem = optionalOrderItem.get();
            Integer count = orderItem.getCount();

            if (count > 1) {
                orderItem.setCount(orderItem.getCount() - 1);
                orderItemRepository.save(orderItem);
            } else {
                orderItemRepository.delete(orderItem);
            }
        }
    }

    private void deleteItemFromOrder(Long itemId, Order order) {
        Optional<OrderItem> optionalOrderItem = orderItemRepository.getOrderItemByOrderIdAndItemId(order.getId(), itemId);
        optionalOrderItem.ifPresent(orderItemRepository::delete);
    }
}
