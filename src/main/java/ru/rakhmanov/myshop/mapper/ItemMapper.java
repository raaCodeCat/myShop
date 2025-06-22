package ru.rakhmanov.myshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.rakhmanov.myshop.dto.db.ItemWithCount;
import ru.rakhmanov.myshop.dto.response.ItemDto;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "title", source = "item.name")
    ItemDto matToItemDto(ItemWithCount item);

}
