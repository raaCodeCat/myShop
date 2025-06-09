package ru.rakhmanov.myshop.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.service.ItemService;
import ru.rakhmanov.myshop.service.OrderItemService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private Model model;

    @InjectMocks
    private ItemController itemController;

    @Test
    void showItem_ShouldReturnItemViewWithItemAttribute() {
        Long itemId = 1L;
        ItemDto mockItem = new ItemDto(itemId, "Test Item", BigDecimal.valueOf(100), 1);
        when(itemService.getItemDtoById(itemId)).thenReturn(mockItem);

        String viewName = itemController.showItem(itemId, model);

        assertEquals("item", viewName);
        verify(model).addAttribute("item", mockItem);
        verify(itemService).getItemDtoById(itemId);
    }

    @Test
    void editItemInCurrentOrder_ShouldRedirectToItemPage() {
        Long itemId = 1L;
        String action = "add";

        String redirectUrl = itemController.editItemInCurrentOrder(itemId, action);

        assertEquals("redirect:/items/1", redirectUrl);
        verify(orderItemService).editItemInCurrentOrder(itemId, action);
    }
}