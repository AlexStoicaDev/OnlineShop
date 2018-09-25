package ro.msg.learning.shop.exceptions;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final Object actual;

    public BaseException(String msg, Object actual) {
        super(msg);
        this.actual = actual;

    }
}
