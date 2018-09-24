package ro.msg.learning.shop.handlers;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ro.msg.learning.shop.exceptions.LocationNotFoundException;
import ro.msg.learning.shop.exceptions.StrategyNotFoundException;

@RestControllerAdvice
public class ShopExceptionHandler {

    @ExceptionHandler(StrategyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorMessage strategyNotFoundException(StrategyNotFoundException ex) {
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(LocationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorMessage locationNotFoundException(LocationNotFoundException ex) {
        return new ErrorMessage(ex.getMessage());
    }

    @Data
    @AllArgsConstructor
    private static class ErrorMessage {
        private String message;
    }
}
