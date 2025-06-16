package ru.rakhmanov.myshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.rakhmanov.myshop.dto.entity.Item;
import ru.rakhmanov.myshop.dto.entity.OrderItem;
import ru.rakhmanov.myshop.dto.response.ItemDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "title", source = "item.name")
    ItemDto matToItemDto(Item item, Integer count);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "title", source = "item.name")
    @Mapping(target = "description", source = "item.description")
    @Mapping(target = "price", source = "item.price")
    @Mapping(target = "imagePath", source = "item.imagePath")
    ItemDto matToItemDto(OrderItem orderItem);

    List<ItemDto> matToItemDto(List<OrderItem> orderItems);

}
