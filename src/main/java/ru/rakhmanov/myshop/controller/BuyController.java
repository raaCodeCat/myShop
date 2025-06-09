package ru.rakhmanov.myshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rakhmanov.myshop.service.OrderService;

import java.util.Optional;

@Controller
@RequestMapping("/buy")
@RequiredArgsConstructor
public class BuyController {

    private final OrderService orderService;

    @PostMapping()
    public String buy() {
        Optional<Long> optOrderId = orderService.getCurrentOrderId();

        return optOrderId.map(aLong -> String.format("redirect:/orders/%d?newOrder=true", aLong)).orElse("error");
    }

}
