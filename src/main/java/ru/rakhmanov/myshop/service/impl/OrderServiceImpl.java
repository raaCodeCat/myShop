package ru.rakhmanov.myshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.dto.db.view.OrderDetails;
import ru.rakhmanov.myshop.dto.response.OrderDto;
import ru.rakhmanov.myshop.exeption.NotFoundException;
import ru.rakhmanov.myshop.mapper.OrderMapper;
import ru.rakhmanov.myshop.repository.OrderDetailsDAO;
import ru.rakhmanov.myshop.service.OrderService;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderDetailsDAO orderDetailsDAO;
    private final OrderMapper orderMapper;

    @Override
    public Mono<OrderDto> getOrderById(Long id) {

        return orderDetailsDAO.existsByOrderId(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new NotFoundException("Заказ не найден"));
                    }
                    return Mono.zip(
                            orderDetailsDAO.getOrderById(id).collectList(),
                            orderDetailsDAO.getOrderTotalSum(id).defaultIfEmpty(BigDecimal.ZERO)
                    ).map(tuple -> {
                        List<OrderDetails> details = tuple.getT1();
                        BigDecimal total = tuple.getT2();
                        return orderMapper.mapToOrderDto(id, details, total);
                    });
                });
    }
}
