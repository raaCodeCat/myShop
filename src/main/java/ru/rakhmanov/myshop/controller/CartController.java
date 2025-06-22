package ru.rakhmanov.myshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.service.OrderItemService;
import ru.rakhmanov.myshop.service.OrderService;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final OrderItemService orderItemService;
    private final OrderService orderService;

    @GetMapping("/items")
    public Mono<String> cart(Model model) {
        return orderService.getCart()
                .flatMap(order -> {
                    model.addAttribute("items", order.getItems());
                    model.addAttribute("total", order.getTotalSum());
                    model.addAttribute("empty", order.getItems().isEmpty());
                    return Mono.just("cart");
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
                            .thenReturn("redirect:/cart/items");
                });
    }

}
