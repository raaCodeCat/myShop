package ru.rakhmanov.myshop.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.rakhmanov.myshop.dto.PagingData;
import ru.rakhmanov.myshop.dto.SortTypeEnum;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.service.ItemService;
import ru.rakhmanov.myshop.service.MainService;
import ru.rakhmanov.myshop.service.OrderItemService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {

    private static final String DEFAULT_SORT_TYPE = "NO";
    private static final String DEFAULT_PAGE_SIZE = "10";
    private static final String DEFAULT_PAGE_NUMBER = "0";

    private final MainService mainService;
    private final ItemService itemService;
    private final OrderItemService orderItemService;

    @GetMapping("/items")
    public String getMainPage(
            @RequestParam(name = "search", defaultValue = StringUtils.EMPTY) String search,
            @RequestParam(name = "sort", defaultValue = DEFAULT_SORT_TYPE) SortTypeEnum sort,
            @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "pageNumber", defaultValue = DEFAULT_PAGE_NUMBER) Integer pageNumber,
            Model model
    ) {
        List<ItemDto> items = mainService.getItemsPageable(search, sort, pageSize, pageNumber);
        Integer itemCount = itemService.getItemsCount(search);

        boolean hasPrevious = pageNumber > 0;
        boolean hasNext = (pageNumber + 1) * pageSize < itemCount;

        PagingData paging = new PagingData(pageNumber, pageSize, hasNext, hasPrevious);

        model.addAttribute("search", Optional.ofNullable(search).orElse(StringUtils.EMPTY));
        model.addAttribute("sort", sort.name());
        model.addAttribute("paging", paging);
        model.addAttribute("items", items);

        return "main";
    }

    @PostMapping("/items/{id}")
    public String editItemInCurrentOrder(
            @PathVariable(name = "id") Long itemId,
            @RequestParam(name = "action") String action
    ) {
        orderItemService.editItemInCurrentOrder(itemId, action);

        return "redirect:/main/items";
    }

}
