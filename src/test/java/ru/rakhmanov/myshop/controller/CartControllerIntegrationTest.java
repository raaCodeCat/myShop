package ru.rakhmanov.myshop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.service.CartService;
import ru.rakhmanov.myshop.service.OrderItemService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private OrderItemService orderItemService;

    @Test
    void getCartItems_ShouldReturnCartView() throws Exception {
        List<ItemDto> mockItems = List.of(
                new ItemDto(1L, "Item 1", BigDecimal.valueOf(100), 1),
                new ItemDto(2L, "Item 2", BigDecimal.valueOf(200), 2)
        );
        BigDecimal mockTotal = BigDecimal.valueOf(500);

        when(cartService.getCart()).thenReturn(mockItems);
        when(cartService.getTotal(mockItems)).thenReturn(mockTotal);

        mockMvc.perform(get("/cart/items"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attributeExists("items"))
                .andExpect(model().attributeExists("total"))
                .andExpect(model().attributeExists("empty"));
    }

    @Test
    void editItemInCurrentOrder_ShouldRedirectToCartItems() throws Exception {
        Long itemId = 1L;
        String action = "increase";

        mockMvc.perform(post("/cart/items/{id}", itemId)
                        .param("action", action))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/items"));

        verify(orderItemService).editItemInCurrentOrder(itemId, action);
    }

    @Test
    void editItemInCurrentOrder_WithInvalidAction_ShouldReturnBadRequest() throws Exception {
        Long itemId = 1L;
        String invalidAction = "invalid_action";

        doThrow(new IllegalArgumentException("Invalid action"))
                .when(orderItemService).editItemInCurrentOrder(itemId, invalidAction);

        mockMvc.perform(post("/cart/items/{id}", itemId)
                        .param("action", invalidAction))
                .andExpect(view().name("error"));
    }
}