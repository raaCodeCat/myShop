package ru.rakhmanov.myshop.errorhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.exeption.NotFoundException;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public Mono<Rendering> handleNotFoundException(NotFoundException ex) {
        return Mono.just(
                Rendering.view("error404")
                        .modelAttribute("message", ex.getMessage())
                        .status(HttpStatus.NOT_FOUND)
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public Mono<Rendering> handleInternalServerError(Exception ex) {
        return Mono.just(
                Rendering.view("error")
                        .modelAttribute("message", "Произошла внутренняя ошибка сервера")
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build()
        );
    }
}
