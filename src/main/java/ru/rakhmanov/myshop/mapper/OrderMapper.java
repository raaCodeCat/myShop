package ru.rakhmanov.myshop.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.rakhmanov.myshop.dto.entity.OrderItem;
import ru.rakhmanov.myshop.dto.response.OrderDto;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring", uses = {ItemMapper.class})
public interface OrderMapper {

    @Mapping(target = "items", source = "orderItems")
    OrderDto mapToOrderDto(Long id, List<OrderItem> orderItems);

    @AfterMapping
    default void setOrderTotal(@MappingTarget OrderDto orderDto) {
        BigDecimal total = orderDto.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        orderDto.setTotalSum(total);
    }

}
