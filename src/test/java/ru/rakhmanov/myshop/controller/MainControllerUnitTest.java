package ru.rakhmanov.myshop.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.rakhmanov.myshop.dto.PagingData;
import ru.rakhmanov.myshop.dto.SortTypeEnum;
import ru.rakhmanov.myshop.dto.response.ItemDto;
import ru.rakhmanov.myshop.service.ItemService;
import ru.rakhmanov.myshop.service.MainService;
import ru.rakhmanov.myshop.service.OrderItemService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MainControllerUnitTest {
    @Mock
    private MainService mainService;
    @Mock
    private ItemService itemService;
    @Mock
    private OrderItemService orderItemService;
    @Mock
    private Model model;
    @Mock
    private ServerWebExchange exchange;

    @InjectMocks
    private MainController mainController;

    @Test
    void getMainPage_WithDefaultParams_ReturnsMainView() {
        List<ItemDto> items = Collections.singletonList(new ItemDto());
        when(mainService.getItemsPageable("", SortTypeEnum.NO, 10, 0))
                .thenReturn(Flux.fromIterable(items));
        when(itemService.getItemsCount("")).thenReturn(Mono.just(1));

        ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/main/items"));

        Mono<String> result = mainController.getMainPage(exchange, model);

        StepVerifier.create(result)
                .expectNext("main")
                .verifyComplete();

        verify(model).addAttribute(eq("items"), any(List.class));
        verify(model).addAttribute(eq("search"), anyString());
        verify(model).addAttribute(eq("sort"), anyString());
        verify(model).addAttribute(eq("paging"), any(PagingData.class));
    }

    @Test
    void editItemInCurrentOrder_ShouldRedirect_WhenActionIsValid() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("action", "add");
        when(exchange.getFormData()).thenReturn(Mono.just(formData));
        when(orderItemService.editItemInCurrentOrder(any(), any())).thenReturn(Mono.empty());

        Mono<String> result = mainController.editItemInCurrentOrder(1L, exchange);

        StepVerifier.create(result)
                .expectNext("redirect:/main/items")
                .verifyComplete();
    }

    @Test
    void editItemInCurrentOrder_ShouldThrowException_WhenActionIsMissing() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        when(exchange.getFormData()).thenReturn(Mono.just(formData));

        Mono<String> result = mainController.editItemInCurrentOrder(1L, exchange);

        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}