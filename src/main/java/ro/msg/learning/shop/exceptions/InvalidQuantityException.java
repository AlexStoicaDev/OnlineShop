package ro.msg.learning.shop.exceptions;


public class InvalidQuantityException extends BaseException {

    public InvalidQuantityException(String msg, Object actual) {
        super(msg, actual);

    }
}
