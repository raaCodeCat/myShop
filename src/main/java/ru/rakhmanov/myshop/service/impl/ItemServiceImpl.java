package ru.rakhmanov.myshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.rakhmanov.myshop.dto.SortTypeEnum;
import ru.rakhmanov.myshop.dto.entity.Item;
import ru.rakhmanov.myshop.repository.ItemRepository;
import ru.rakhmanov.myshop.service.ItemService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public List<Item> getItemsPageable(String search, SortTypeEnum sortType, Integer pageSize, Integer pageNumber) {

        Sort sort = switch (sortType) {
            case ALPHA -> Sort.by(Sort.Direction.ASC, "name");
            case PRICE -> Sort.by(Sort.Direction.ASC, "price");
            default -> Sort.unsorted();
        };

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        return itemRepository.getItemsByNameLikeIgnoreCase(makeSearchString(search), pageable);
    }

    @Override
    public Integer getItemsCount(String search) {
        return itemRepository.countItemsByNameLikeIgnoreCase(makeSearchString(search));
    }

    private String makeSearchString(String search) {
        return "%" + search + "%";
    }
}
