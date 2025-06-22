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
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.service.ItemService;
import ru.rakhmanov.myshop.service.OrderItemService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemControllerUnitTest {

    @Mock
    private ItemService itemService;

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private Model model;

    @Mock
    private ServerWebExchange exchange;

    @InjectMocks
    private ItemController itemController;

    @Test
    void showItem_ShouldReturnViewWithItemData() {
        ItemDto item = new ItemDto();
        item.setId(1L);
        when(itemService.getItemById(1L)).thenReturn(Mono.just(item));

        Mono<String> result = itemController.showItem(1L, model);

        StepVerifier.create(result)
                .expectNext("item")
                .verifyComplete();

        verify(model).addAttribute(eq("item"), eq(item));
    }

    @Test
    void editItemInCurrentOrder_ShouldRedirect_WhenActionIsValid() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("action", "add");
        when(exchange.getFormData()).thenReturn(Mono.just(formData));
        when(orderItemService.editItemInCurrentOrder(anyLong(), any())).thenReturn(Mono.empty());

        Mono<String> result = itemController.editItemInCurrentOrder(1L, exchange);

        StepVerifier.create(result)
                .expectNext("redirect:/items/1")
                .verifyComplete();
    }

    @Test
    void editItemInCurrentOrder_ShouldThrowException_WhenActionIsMissing() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        when(exchange.getFormData()).thenReturn(Mono.just(formData));

        Mono<String> result = itemController.editItemInCurrentOrder(1L, exchange);

        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}