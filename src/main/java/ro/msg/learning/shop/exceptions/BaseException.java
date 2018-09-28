package ro.msg.learning.shop.exceptions;

import lombok.Getter;

@Getter
class BaseException extends RuntimeException {

    private final Object actual;

    BaseException(String msg, Object actual) {
        super(msg);
        this.actual = actual;

    }
}
