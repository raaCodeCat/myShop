package ru.rakhmanov.myshop.service;

import ru.rakhmanov.myshop.dto.entity.Order;

public interface OrderService {

    Order getCurrentOrderByClientId(Long clientId);

}
