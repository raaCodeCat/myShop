package ru.rakhmanov.myshop.service;

import ru.rakhmanov.myshop.dto.response.ItemDto;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {

    List<ItemDto> getCart();

    BigDecimal getTotal(List<ItemDto> items);

}
