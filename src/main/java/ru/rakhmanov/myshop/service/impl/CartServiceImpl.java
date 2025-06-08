package ru.rakhmanov.myshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rakhmanov.myshop.dto.entity.Order;
import ru.rakhmanov.myshop.dto.entity.OrderItem;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.mapper.OrderItemMapper;
import ru.rakhmanov.myshop.service.CartService;
import ru.rakhmanov.myshop.service.OrderItemService;
import ru.rakhmanov.myshop.service.OrderService;
import ru.rakhmanov.myshop.utils.RequestHeaderUtil;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<ItemDto> getCart() {
        Long clientId = RequestHeaderUtil.getClientId();
        Order order = orderService.getCurrentOrderByClientId(clientId);

        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderId(order.getId());

        return orderItemMapper.toItemDto(orderItems);
    }

    @Override
    public BigDecimal getTotal(List<ItemDto> items) {
        return items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
