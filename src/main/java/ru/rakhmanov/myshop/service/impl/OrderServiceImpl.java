package ru.rakhmanov.myshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rakhmanov.myshop.dto.entity.Order;
import ru.rakhmanov.myshop.dto.entity.OrderItem;
import ru.rakhmanov.myshop.dto.response.OrderDto;
import ru.rakhmanov.myshop.exeption.NotFoundException;
import ru.rakhmanov.myshop.mapper.OrderMapper;
import ru.rakhmanov.myshop.repository.OrderItemRepository;
import ru.rakhmanov.myshop.repository.OrderRepository;
import ru.rakhmanov.myshop.service.OrderService;
import ru.rakhmanov.myshop.utils.RequestHeaderUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;

    @Override
    public Order getCurrentOrderByClientId(Long clientId) {
        Optional<Order> optionalOrder = orderRepository.findOrderByUserIdAndIsPaidFalse(clientId);

        if (optionalOrder.isEmpty()) {
            Order order = new Order(clientId);
            orderRepository.save(order);

            return order;
        }

        return optionalOrder.get();
    }

    @Override
    public Optional<Long> getCurrentOrderId() {
        Long clientId = RequestHeaderUtil.getClientId();

        return orderRepository.findOrderByUserIdAndIsPaidFalse(clientId)
                .map(Order::getId);
    }

    @Override
    public OrderDto getOrderById(Long id) {
        List<OrderItem> orderItems = orderItemRepository.getOrderItemsByOrderIdFull(id);
        Order order = orderItems.stream()
                .findFirst()
                .map(OrderItem::getOrder).orElseThrow(NotFoundException::new);


        return orderMapper.mapToOrderDto(order.getId(), orderItems);
    }

    @Override
    public void buyOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(NotFoundException::new);
        order.setIsPaid(true);
        orderRepository.save(order);
    }

    @Override
    public List<OrderDto> getOrdersByClient() {
        Long clientId = RequestHeaderUtil.getClientId();
        List<OrderItem> orderItems = orderItemRepository.getOrderItemsByClientIdFull(clientId);
        Map<Long, List<OrderItem>> mapOrderItems = orderItems.stream()
                .collect(Collectors.groupingBy(
                        orderItem -> orderItem.getOrder().getId()
                ));

        return mapOrderItems.entrySet().stream()
                .map(entry -> orderMapper.mapToOrderDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
