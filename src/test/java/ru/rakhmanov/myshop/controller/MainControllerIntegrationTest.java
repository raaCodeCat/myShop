package ru.rakhmanov.myshop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.service.ItemService;
import ru.rakhmanov.myshop.service.MainService;
import ru.rakhmanov.myshop.service.OrderItemService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MainService mainService;

    @MockitoBean
    private ItemService itemService;

    @MockitoBean
    private OrderItemService orderItemService;

    @Test
    void getMainPage_ShouldReturnMainView() throws Exception {
        List<ItemDto> mockItems = Collections.singletonList(
                new ItemDto(1L, "Test Item", BigDecimal.valueOf(100), 1)
        );
        when(mainService.getItemsPageable(anyString(), any(), anyInt(), anyInt())).thenReturn(mockItems);
        when(itemService.getItemsCount(anyString())).thenReturn(1);

        mockMvc.perform(get("/main/items"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(model().attributeExists("items"))
                .andExpect(model().attributeExists("paging"));
    }

    @Test
    void getMainPage_WithSearchParam_ShouldPassSearchToService() throws Exception {
        mockMvc.perform(get("/main/items").param("search", "test"))
                .andExpect(status().isOk());
    }

    @Test
    void editItemInCurrentOrder_ShouldRedirectToMainItems() throws Exception {
        mockMvc.perform(post("/main/items/1").param("action", "add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/main/items"));
    }
}