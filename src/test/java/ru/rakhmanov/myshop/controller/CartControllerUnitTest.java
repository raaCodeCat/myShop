package ru.rakhmanov.myshop.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.rakhmanov.myshop.dto.response.OrderDto;
import ru.rakhmanov.myshop.service.OrderItemService;
import ru.rakhmanov.myshop.service.OrderService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartControllerUnitTest {

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private OrderService orderService;

    @Mock
    private Model model;

    @Mock
    private ServerWebExchange exchange;

    @InjectMocks
    private CartController cartController;

    @Test
    void cart_ShouldReturnViewWithOrderData() {
        OrderDto order = new OrderDto();
        order.setItems(Collections.emptyList());
        when(orderService.getCart()).thenReturn(Mono.just(order));

        Mono<String> result = cartController.cart(model);

        StepVerifier.create(result)
                .expectNext("cart")
                .verifyComplete();

        verify(model).addAttribute(eq("items"), eq(Collections.emptyList()));
        verify(model).addAttribute(eq("total"), any());
        verify(model).addAttribute(eq("empty"), eq(true));
    }

    @Test
    void editItemInCurrentOrder_ShouldRedirect_WhenActionIsValid() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("action", "increase");

        when(exchange.getFormData()).thenReturn(Mono.just(formData));
        when(orderItemService.editItemInCurrentOrder(anyLong(), any())).thenReturn(Mono.empty());

        Mono<String> result = cartController.editItemInCurrentOrder(1L, exchange);

        StepVerifier.create(result)
                .expectNext("redirect:/cart/items")
                .verifyComplete();
    }

    @Test
    void editItemInCurrentOrder_ShouldThrowException_WhenActionIsMissing() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        when(exchange.getFormData()).thenReturn(Mono.just(formData));

        Mono<String> result = cartController.editItemInCurrentOrder(1L, exchange);

        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}