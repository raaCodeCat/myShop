package ru.rakhmanov.myshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.rakhmanov.myshop.dto.entity.Item;
import ru.rakhmanov.myshop.dto.response.ItemDto;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "title", source = "item.name")
    ItemDto matToItemDto(Item item, Integer count);

}
