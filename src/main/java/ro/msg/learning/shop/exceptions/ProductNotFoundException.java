package ro.msg.learning.shop.exceptions;

public class ProductNotFoundException extends BaseException {
    public ProductNotFoundException(String msg, Object actual) {
        super(msg, actual);
    }
}
