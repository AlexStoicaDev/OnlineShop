package ro.msg.learning.shop.exceptions;

public class InvalidPasswordException extends BaseException {

    public InvalidPasswordException(String msg, Object actual) {
        super(msg, actual);
    }
}
