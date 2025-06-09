package ru.rakhmanov.myshop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.rakhmanov.myshop.service.OrderService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class BuyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Test
    void buy_WhenOrderExists_ShouldRedirectToOrderPage() throws Exception {
        Long orderId = 123L;
        when(orderService.getCurrentOrderId()).thenReturn(Optional.of(orderId));

        mockMvc.perform(post("/buy"))
                .andExpect(redirectedUrl("/orders/123?newOrder=true"));
    }

    @Test
    void buy_WhenOrderDoesNotExist_ShouldReturnErrorView() throws Exception {
        when(orderService.getCurrentOrderId()).thenReturn(Optional.empty());

        mockMvc.perform(post("/buy"))
                .andExpect(view().name("error"));
    }
}