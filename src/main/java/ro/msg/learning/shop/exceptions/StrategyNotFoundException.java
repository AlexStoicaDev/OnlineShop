package ro.msg.learning.shop.exceptions;


import lombok.Data;

@Data
public class StrategyNotFoundException extends RuntimeException {
    private final String context;

    public StrategyNotFoundException(String context) {

        super("StrategyNotFoundException");
        this.context = context;
    }
}
