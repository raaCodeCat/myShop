package ru.rakhmanov.myshop.errorhandler;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.rakhmanov.myshop.exeption.NotFoundException;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error404";
    }

    @ExceptionHandler(Exception.class)
    public String handleIternalServerError(Exception ex) {
        return "error";
    }
}
