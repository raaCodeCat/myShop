package ru.rakhmanov.myshop.service;

import ru.rakhmanov.myshop.dto.entity.Order;
import ru.rakhmanov.myshop.dto.response.OrderDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Order getCurrentOrderByClientId(Long clientId);

    Optional<Long> getCurrentOrderId();

    OrderDto getOrderById(Long id);

    void buyOrder(Long orderId);

    List<OrderDto> getOrdersByClient();

}
