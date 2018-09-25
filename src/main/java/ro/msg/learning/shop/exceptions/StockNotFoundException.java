package ro.msg.learning.shop.exceptions;


public class StockNotFoundException extends BaseException {
    public StockNotFoundException(String msg, Object actual) {
        super(msg, actual);

    }
}
