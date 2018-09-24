package ro.msg.learning.shop.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StrategyNotFoundException extends RuntimeException {
    public StrategyNotFoundException(String message) {
        super(message);
    }
}
