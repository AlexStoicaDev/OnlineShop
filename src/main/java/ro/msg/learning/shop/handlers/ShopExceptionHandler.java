package ro.msg.learning.shop.handlers;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ro.msg.learning.shop.exceptions.*;

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
    protected ErrorMessage invalidQuantityException(InvalidQuantityException ex) {
        return new ErrorMessage(ex.getMessage(), "Integer greater than zero", ex.getActual());
    }


    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorMessage invalidPasswordException(InvalidPasswordException ex) {

        return new ErrorMessage(ex.getMessage(), "a digit must occur at least once" +
            "a lower case letter must occur at least once" +
            " an upper case letter must occur at least once" +
            " a special character must occur at least once" +
            " no whitespace allowed in the entire string" +
            "anything, at least eight places though",
            ex.getActual());
    }

    @ExceptionHandler(InvalidUsernameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorMessage invalidUsernameException(InvalidUsernameException ex) {
        return new ErrorMessage(ex.getMessage(), "sadsadsadsa", ex.getActual());
    }

    @Data
    @AllArgsConstructor
    private static class ErrorMessage {
        private String message;
        private Object expected;
        private Object actual;
    }
}
