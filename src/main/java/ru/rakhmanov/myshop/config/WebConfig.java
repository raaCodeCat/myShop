package ru.rakhmanov.myshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class WebConfig {

    @Bean
    public RouterFunction<ServerResponse> redirectRouter() {
        return route(GET("/"),
                req -> ServerResponse.permanentRedirect(req.uri().resolve("/main/items")).build());
    }
}
