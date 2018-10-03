package ro.msg.learning.shop.validators;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.exceptions.InvalidUsernameException;
import ro.msg.learning.shop.repositories.CustomerRepository;


@Component
public class UsernameValidator {

    private static CustomerRepository customerRepository;

    @Autowired
    public UsernameValidator(CustomerRepository customerRepository) {
        customerRepository = customerRepository;
    }


    public static void validate(String username) {
        if ((username).length() < 4) {
            throw new InvalidUsernameException("username length < 4", "dada");
        }
        if (customerRepository.findCustomerByUserName((username)) != null) {
            throw new InvalidUsernameException("mai apare boss", "dada");
        }
    }
}
