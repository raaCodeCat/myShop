package ru.rakhmanov.myshop.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.service.CartService;
import ru.rakhmanov.myshop.service.OrderItemService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private Model model;

    @InjectMocks
    private CartController cartController;

    @Test
    void cart_ShouldReturnCartViewWithAttributes() {
        List<ItemDto> mockItems = List.of(
                new ItemDto(1L, "Item 1", BigDecimal.valueOf(100), 1),
                new ItemDto(2L, "Item 2", BigDecimal.valueOf(200), 2)
        );
        BigDecimal mockTotal = BigDecimal.valueOf(500);

        when(cartService.getCart()).thenReturn(mockItems);
        when(cartService.getTotal(mockItems)).thenReturn(mockTotal);

        String viewName = cartController.cart(model);

        assertEquals("cart", viewName);
        verify(model).addAttribute("items", mockItems);
        verify(model).addAttribute("total", mockTotal);
        verify(model).addAttribute("empty", false);
        verify(cartService).getCart();
        verify(cartService).getTotal(mockItems);
    }

    @Test
    void cart_ShouldShowEmptyCartWhenNoItems() {
        List<ItemDto> emptyItems = List.of();
        when(cartService.getCart()).thenReturn(emptyItems);
        when(cartService.getTotal(emptyItems)).thenReturn(BigDecimal.ZERO);

        String viewName = cartController.cart(model);

        assertEquals("cart", viewName);
        verify(model).addAttribute("empty", true);
    }

    @Test
    void editItemInCurrentOrder_ShouldRedirectToCartItems() {
        Long itemId = 1L;
        String action = "increase";

        String redirectUrl = cartController.editItemInCurrentOrder(itemId, action);

        assertEquals("redirect:/cart/items", redirectUrl);
        verify(orderItemService).editItemInCurrentOrder(itemId, action);
    }
}