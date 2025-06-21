package ru.rakhmanov.myshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.service.OrderService;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    public Mono<Rendering> showOrder(
            @PathVariable(name = "id") Long orderId,
            @RequestParam(name = "newOrder", defaultValue = "false") Boolean newOrder
    ) {
        return orderService.getOrderById(orderId)
                .map(orderDetails -> Rendering.view("order")
                        .modelAttribute("order", orderDetails)
                        .modelAttribute("newOrder", newOrder)
                        .build());
    }

}
