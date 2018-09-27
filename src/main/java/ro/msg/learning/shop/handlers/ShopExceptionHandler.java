package ro.msg.learning.shop.handlers;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ro.msg.learning.shop.exceptions.FileTypeMismatchException;
import ro.msg.learning.shop.exceptions.InvalidQuantityException;
import ro.msg.learning.shop.exceptions.StockNotFoundException;
import ro.msg.learning.shop.exceptions.StrategyNotFoundException;

@RestControllerAdvice

public class ShopExceptionHandler {

    @ExceptionHandler(StrategyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorMessage strategyNotFoundException(StrategyNotFoundException ex) {
        return new ErrorMessage(ex.getMessage(), "To find a strategy", "actualStrategyNotFoundException");
    }

    @ExceptionHandler(StockNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorMessage stockNotFoundException(StockNotFoundException ex) {
        return new ErrorMessage(ex.getMessage(), "To find a stock", ex.getActual());
    }

    @ExceptionHandler(FileTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorMessage fileTypeMismatchException(FileTypeMismatchException ex) {
        return new ErrorMessage(ex.getMessage(), "CSV file type", ex.getActual());
    }

    @ExceptionHandler(InvalidQuantityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorMessage quantityNotGreaterThanZeroException(InvalidQuantityException ex) {
        return new ErrorMessage(ex.getMessage(), "Integer greater than zero", ex.getActual());
    }

    @Data
    @AllArgsConstructor
    public static class ErrorMessage {
        private String message;
        private Object expected;
        private Object actual;
    }
}
