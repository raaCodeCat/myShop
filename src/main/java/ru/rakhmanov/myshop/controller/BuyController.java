package ru.rakhmanov.myshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.service.OrderService;

@Controller
@RequestMapping("/buy")
@RequiredArgsConstructor
public class BuyController {

    private final OrderService orderService;

    @PostMapping
    public Mono<String> buy() {
        return orderService.getCurrentOrderId()
                .map(orderId -> String.format("redirect:/orders/%d?newOrder=true", orderId))
                .defaultIfEmpty("error");
    }

}
