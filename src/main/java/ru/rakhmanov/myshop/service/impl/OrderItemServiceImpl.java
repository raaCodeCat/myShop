package ru.rakhmanov.myshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rakhmanov.myshop.dto.entity.Order;
import ru.rakhmanov.myshop.dto.entity.OrderItem;
import ru.rakhmanov.myshop.repository.OrderItemRepository;
import ru.rakhmanov.myshop.service.OrderItemService;
import ru.rakhmanov.myshop.service.OrderService;
import ru.rakhmanov.myshop.utils.RequestHeaderUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
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
}
