package ru.rakhmanov.myshop.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.service.ItemService;
import ru.rakhmanov.myshop.service.OrderItemService;

@Controller
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final OrderItemService orderItemService;

    @GetMapping("/{itemId}")
    public Mono<String> showItem(
            @PathVariable(name = "itemId") Long itemId,
            Model model
    ) {
        return itemService.getItemById(itemId)
                .flatMap(item -> {
                    model.addAttribute("item", item);
                    return Mono.just("item");
                });
    }

    @PostMapping("/{itemId}")
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
                            .thenReturn("redirect:/items/" + itemId);
                });
    }

}
