package ro.msg.learning.shop.exceptions;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String msg, Object actual) {
        super(msg, actual);

    }
}
