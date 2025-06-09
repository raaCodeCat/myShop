package ru.rakhmanov.myshop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.rakhmanov.myshop.dto.response.OrderDto;
import ru.rakhmanov.myshop.service.OrderService;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Test
    void showOrder_ShouldReturnOrderPage() throws Exception {
        Long orderId = 1L;
        OrderDto mockOrder = new OrderDto();
        when(orderService.getOrderById(orderId)).thenReturn(mockOrder);

        mockMvc.perform(get("/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(view().name("order"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attribute("newOrder", false));
    }

    @Test
    void showOrder_WithNewOrderParam_ShouldPassParameter() throws Exception {
        Long orderId = 1L;
        OrderDto mockOrder = new OrderDto();
        when(orderService.getOrderById(orderId)).thenReturn(mockOrder);

        mockMvc.perform(get("/orders/{id}?newOrder=true", orderId))
                .andExpect(model().attribute("newOrder", true));
    }

    @Test
    void showAllOrders_ShouldReturnOrdersPage() throws Exception {
        List<OrderDto> mockOrders = Collections.singletonList(new OrderDto());
        when(orderService.getOrdersByClient()).thenReturn(mockOrders);

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attributeExists("orders"));
    }
}