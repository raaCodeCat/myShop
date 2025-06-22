package ru.rakhmanov.myshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
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
@Slf4j
public class MainController {

    private static final String DEFAULT_SORT_TYPE = "NO";
    private static final String DEFAULT_PAGE_SIZE = "10";
    private static final String DEFAULT_PAGE_NUMBER = "0";
    private final OrderItemService orderItemService;

    private final MainService mainService;
    private final ItemService itemService;

    @GetMapping("/items")
    public Mono<String> getMainPage(ServerWebExchange exchange, Model model) {
        return Mono.fromCallable(() -> {
                    var params = exchange.getRequest().getQueryParams();

                    String search = params.getFirst("search") != null ? params.getFirst("search") : StringUtils.EMPTY;
                    SortTypeEnum sort = SortTypeEnum.valueOf(
                            params.getFirst("sort") != null ? params.getFirst("sort") : DEFAULT_SORT_TYPE
                    );
                    int pageSize = Integer.parseInt(
                            params.getFirst("pageSize") != null ? params.getFirst("pageSize") : DEFAULT_PAGE_SIZE
                    );
                    int pageNumber = Integer.parseInt(
                            params.getFirst("pageNumber") != null ? params.getFirst("pageNumber") : DEFAULT_PAGE_NUMBER
                    );

                    return new Object[]{search, sort, pageSize, pageNumber};
                })
                .flatMap(params -> {
                    String search = (String) params[0];
                    SortTypeEnum sort = (SortTypeEnum) params[1];
                    int pageSize = (int) params[2];
                    int pageNumber = (int) params[3];

                    return mainService.getItemsPageable(search, sort, pageSize, pageNumber)
                            .doOnNext(item -> log.info("Loaded item: {}", item.getId()))
                            .collectList()
                            .zipWith(itemService.getItemsCount(search))
                            .flatMap(tuple -> {
                                List<ItemDto> items = tuple.getT1();
                                Integer itemCount = tuple.getT2();

                                boolean hasPrevious = pageNumber > 0;
                                boolean hasNext = (pageNumber + 1) * pageSize < itemCount;

                                PagingData paging = new PagingData(pageNumber, pageSize, hasNext, hasPrevious);

                                model.addAttribute("items", items);
                                model.addAttribute("search", Optional.ofNullable(search).orElse(StringUtils.EMPTY));
                                model.addAttribute("sort", sort.name());
                                model.addAttribute("paging", paging);


                                return Mono.just("main");
                            });
                });
    }

    @PostMapping("/items/{itemId}")
    public Mono<String> editItemInCurrentOrder(
            @PathVariable Long itemId,
            ServerWebExchange exchange
    ) {
        return exchange.getFormData()
                .cache()
                .flatMap(formData -> {
                    String action = formData.getFirst("action");
                    if (action == null) {
                        return Mono.error(new IllegalArgumentException("Отсутствует парамерт action"));
                    }
                    return orderItemService.editItemInCurrentOrder(itemId, action)
                            .thenReturn("redirect:/main/items");
                });
    }

}
