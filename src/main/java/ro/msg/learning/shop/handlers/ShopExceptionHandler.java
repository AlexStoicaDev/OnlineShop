package ro.msg.learning.shop.handlers;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ro.msg.learning.shop.exceptions.FileTypeMismatchException;
import ro.msg.learning.shop.exceptions.StockNotFoundException;
import ro.msg.learning.shop.exceptions.StrategyNotFoundException;

@RestControllerAdvice
public class ShopExceptionHandler {

    @ExceptionHandler(StrategyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorMessage strategyNotFoundException(StrategyNotFoundException ex) {
        return new ErrorMessage(ex.getMessage(), ex.getContext());
    }

    @ExceptionHandler(StockNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorMessage locationNotFoundException(StockNotFoundException ex) {
        return new ErrorMessage(ex.getMessage(), ex.getContext());
    }

    @ExceptionHandler(FileTypeMismatchException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorMessage fileTypeMismatchException(FileTypeMismatchException ex) {
        return new ErrorMessage(ex.getMessage(), ex.getContext());
    }
    @Data
    @AllArgsConstructor
    private static class ErrorMessage {
        private String message;
        private String context;
    }
}
