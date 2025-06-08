package ru.rakhmanov.myshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.service.CartService;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/items")
    public String cart(Model model) {
        List<ItemDto> items = cartService.getCart();
        BigDecimal total = cartService.getTotal(items);
        boolean isEmpty = items.isEmpty();

        model.addAttribute("items", items);
        model.addAttribute("total", total);
        model.addAttribute("empty", isEmpty);

        return "cart";
    }


}
