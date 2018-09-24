package ro.msg.learning.shop.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(String message) {
        super(message);
    }
}
