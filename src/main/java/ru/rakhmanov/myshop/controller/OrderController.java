package ru.rakhmanov.myshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.rakhmanov.myshop.dto.response.OrderDto;
import ru.rakhmanov.myshop.service.OrderService;

import java.util.List;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    public String showOrder(
        @PathVariable(name = "id") Long orderId,
        @RequestParam(name = "newOrder", defaultValue = "false") Boolean newOrder,
        Model model
    ) {
        orderService.buyOrder(orderId);
        OrderDto order = orderService.getOrderById(orderId);

        model.addAttribute("order", order);
        model.addAttribute("newOrder", newOrder);

        return "order";
    }

    @GetMapping()
    public String showAllOrders(Model model) {
        List<OrderDto> orders = orderService.getOrdersByClient();

        model.addAttribute("orders", orders);

        return "orders";
    }

}
