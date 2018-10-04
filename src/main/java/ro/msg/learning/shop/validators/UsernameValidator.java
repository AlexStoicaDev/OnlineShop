package ro.msg.learning.shop.validators;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.exceptions.InvalidUsernameException;
import ro.msg.learning.shop.repositories.CustomerRepository;


@Component
@RequiredArgsConstructor
public class UsernameValidator {
    private final CustomerRepository customerRepository;

    public void validate(String username) {

        if ((username).length() < 4) {
            throw new InvalidUsernameException("username length < 4", "dada");
        }
        if (customerRepository.existsByUsername(username)) {
            throw new InvalidUsernameException("a fost luat boss", "whateve");
        }

    }
}
