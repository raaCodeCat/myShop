package ru.rakhmanov.myshop.service;

import ru.rakhmanov.myshop.dto.entity.Order;

import java.util.Optional;

public interface OrderService {

    Order getCurrentOrderByClientId(Long clientId);

    Optional<Long> getCurrentOrderId();

}
