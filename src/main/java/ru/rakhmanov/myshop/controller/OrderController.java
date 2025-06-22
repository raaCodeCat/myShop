package ru.rakhmanov.myshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.dto.db.view.OrderDetails;
import ru.rakhmanov.myshop.dto.response.OrderDto;
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

        Mono<OrderDto> orderMono = orderService.getOrderById(orderId);

        if (newOrder) {
            return orderMono
                    .flatMap(orderDetails -> orderService.buyOrder(orderId)
                            .then(orderMono)
                            .map(order -> createRendering(order, true)));
        }

        return orderMono
                .map(order -> createRendering(order, false));
    }

    @GetMapping()
    public Mono<String> showAllOrders(Model model) {
        return orderService.getOrdersByUserId()
                .collectList()
                .flatMap(orders -> {
                    model.addAttribute("orders", orders);
                    return Mono.just("orders");
                });
    }

    private Rendering createRendering(OrderDto order, Boolean newOrder) {
        return Rendering.view("order")
                .modelAttribute("order", order)
                .modelAttribute("newOrder", newOrder)
                .build();
    }

}
