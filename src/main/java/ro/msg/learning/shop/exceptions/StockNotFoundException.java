package ro.msg.learning.shop.exceptions;


import lombok.Data;

@Data
public class StockNotFoundException extends RuntimeException {
    private final String context;

    public StockNotFoundException(String context) {
        super("StockNotFoundException");
        this.context = context;

    }
}
