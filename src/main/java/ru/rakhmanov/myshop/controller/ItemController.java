package ru.rakhmanov.myshop.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.service.ItemService;
import ru.rakhmanov.myshop.service.OrderItemService;

@Controller
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final OrderItemService orderItemService;

    @GetMapping("/{itemId}")
    public String showItem(
            @PathVariable(name = "itemId") Long itemId,
            Model model
    ) {
        ItemDto item = itemService.getItemDtoById(itemId);
        model.addAttribute("item", item);

        return "item";
    }

    @PostMapping("/{id}")
    public String editItemInCurrentOrder(
            @PathVariable(name = "id") Long itemId,
            @RequestParam(name = "action") String action
    ) {
        orderItemService.editItemInCurrentOrder(itemId, action);

        return "redirect:/items/" + itemId;
    }

}
