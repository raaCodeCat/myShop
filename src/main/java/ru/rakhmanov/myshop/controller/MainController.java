package ru.rakhmanov.myshop.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.rakhmanov.myshop.dto.PagingData;
import ru.rakhmanov.myshop.dto.SortTypeEnum;
import ru.rakhmanov.myshop.dto.entity.Item;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.service.ItemService;
import ru.rakhmanov.myshop.service.MainService;

import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;
import java.util.ArrayList;
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

    @GetMapping("/api/check-image")
    public ResponseEntity<String> checkImage() {
        ClassPathResource resource = new ClassPathResource("static/img/gadget_a1.jpg");
        return ResponseEntity.ok(
                resource.exists() ? "Image exists" : "Image NOT FOUND at: " + resource.getPath()
        );
    }

}
