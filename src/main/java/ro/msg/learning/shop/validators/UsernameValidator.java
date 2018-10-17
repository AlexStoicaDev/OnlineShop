package ro.msg.learning.shop.validators;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.exceptions.InvalidUsernameException;
import ro.msg.learning.shop.repositories.CustomerRepository;


@Component
@RequiredArgsConstructor
@Slf4j
/*
 * used in the validation of the username when a customer is created
 */
public class UsernameValidator {

    private final CustomerRepository customerRepository;


    public void validate(String username) {


        if ((username).length() < 4) {

            log.error(" username {} is too short, should be longer than 4 characters , username lenth={}", username, username.length());
            throw new InvalidUsernameException("username length < 4", "");
        }
        //username must not be taken by another user from our db
        if (customerRepository.existsByUsername(username)) {
            log.error("username {} is present in db", username);
            throw new InvalidUsernameException("username is taken", "");
        }

    }
}
