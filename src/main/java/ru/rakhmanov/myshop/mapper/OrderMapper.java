package ru.rakhmanov.myshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.rakhmanov.myshop.dto.db.view.OrderDetails;
import ru.rakhmanov.myshop.dto.response.OrderDto;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "items", source = "details")
    @Mapping(target = "totalSum", source = "totalSum")
    OrderDto mapToOrderDto(Long id, List<OrderDetails> details, BigDecimal totalSum);


}
