package ru.rakhmanov.myshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.rakhmanov.myshop.dto.entity.OrderItem;
import ru.rakhmanov.myshop.dto.response.ItemDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "title", source = "item.name")
    @Mapping(target = "description", source = "item.description")
    @Mapping(target = "price", source = "item.price")
    @Mapping(target = "imagePath", source = "item.imagePath")
    @Mapping(target = "count", source = "count")
    ItemDto toItemDto(OrderItem orderItem);

    List<ItemDto> toItemDto(List<OrderItem> orderItems);

}
