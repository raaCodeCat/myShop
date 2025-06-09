package ru.rakhmanov.myshop.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import ru.rakhmanov.myshop.dto.PagingData;
import ru.rakhmanov.myshop.dto.SortTypeEnum;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.service.ItemService;
import ru.rakhmanov.myshop.service.MainService;
import ru.rakhmanov.myshop.service.OrderItemService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainControllerTest {

    @Mock
    private MainService mainService;

    @Mock
    private ItemService itemService;

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private Model model;

    @InjectMocks
    private MainController mainController;

    @Test
    void getMainPage_ShouldReturnMainViewWithAttributes() {
        List<ItemDto> mockItems = Collections.singletonList(
                new ItemDto(1L, "Test Item", BigDecimal.valueOf(100), 1)
        );
        when(mainService.getItemsPageable(anyString(), any(), anyInt(), anyInt())).thenReturn(mockItems);
        when(itemService.getItemsCount(anyString())).thenReturn(1);

        String viewName = mainController.getMainPage("", SortTypeEnum.NO, 10, 0, model);

        assertEquals("main", viewName);
        verify(model).addAttribute(eq("search"), eq(""));
        verify(model).addAttribute(eq("sort"), eq("NO"));
        verify(model).addAttribute(eq("paging"), any(PagingData.class));
        verify(model).addAttribute(eq("items"), eq(mockItems));
    }

    @Test
    void editItemInCurrentOrder_ShouldRedirectToMainItems() {
        String redirectUrl = mainController.editItemInCurrentOrder(1L, "add");

        assertEquals("redirect:/main/items", redirectUrl);
        verify(orderItemService).editItemInCurrentOrder(1L, "add");
    }
}