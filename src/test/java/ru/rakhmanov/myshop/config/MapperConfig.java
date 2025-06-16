package ru.rakhmanov.myshop.config;

import org.springframework.context.annotation.Bean;
import ru.rakhmanov.myshop.mapper.ItemMapper;
import ru.rakhmanov.myshop.mapper.ItemMapperImpl;
import ru.rakhmanov.myshop.mapper.OrderItemMapper;
import ru.rakhmanov.myshop.mapper.OrderItemMapperImpl;
import ru.rakhmanov.myshop.mapper.OrderMapper;
import ru.rakhmanov.myshop.mapper.OrderMapperImpl;

public class MapperConfig {

    @Bean
    ItemMapper itemMapper() {
        return new ItemMapperImpl();
    }

    @Bean
    OrderItemMapper orderItemMapper() {
        return new OrderItemMapperImpl();
    }

    @Bean
    OrderMapper orderMapper() {
        return new OrderMapperImpl();
    }

}
