package ru.rakhmanov.myshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rakhmanov.myshop.dto.entity.Order;
import ru.rakhmanov.myshop.repository.OrderRepository;
import ru.rakhmanov.myshop.service.OrderService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

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
}
