package ru.rakhmanov.myshop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.exeption.NotFoundException;
import ru.rakhmanov.myshop.service.ItemService;
import ru.rakhmanov.myshop.service.OrderItemService;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemService itemService;

    @MockitoBean
    private OrderItemService orderItemService;

    @Test
    void showItem_ShouldReturnItemPage() throws Exception {
        Long itemId = 1L;
        ItemDto mockItem = new ItemDto(itemId, "Test Item", BigDecimal.valueOf(100), 1);
        when(itemService.getItemDtoById(itemId)).thenReturn(mockItem);

        mockMvc.perform(get("/items/{itemId}", itemId))
                .andExpect(status().isOk())
                .andExpect(view().name("item"))
                .andExpect(model().attributeExists("item"));
    }

    @Test
    void editItemInCurrentOrder_ShouldRedirectToItemPage() throws Exception {
        Long itemId = 1L;
        String action = "add";

        mockMvc.perform(post("/items/{id}", itemId)
                        .param("action", action))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items/" + itemId));
    }

    @Test
    void showItem_ShouldReturnNotFoundForInvalidItem() throws Exception {
        Long invalidItemId = 999L;
        when(itemService.getItemDtoById(invalidItemId)).thenThrow(new NotFoundException("Not found"));

        mockMvc.perform(get("/items/{itemId}", invalidItemId))
                .andExpect(view().name("error404"));
    }
}
