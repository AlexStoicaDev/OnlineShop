package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.customers.CustomerDtoIn;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.entities.Role;
import ro.msg.learning.shop.exceptions.UserNotFoundException;
import ro.msg.learning.shop.mappers.CustomerMapper;
import ro.msg.learning.shop.repositories.CustomerRepository;
import ro.msg.learning.shop.repositories.RoleRepository;
import ro.msg.learning.shop.validators.PasswordValidator;
import ro.msg.learning.shop.validators.UsernameValidator;

import java.util.ArrayList;
import java.util.List;


/**
 * the application logic, regarding the Customer entity
 */
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final UsernameValidator usernameValidator;
    private final PasswordValidator passwordValidator;
    private final PasswordEncoder passwordEncoder;

    /**
     * creates a new customer
     *
     * @param customerDtoIn the customer info is stored in this Dto
     * @return a new Customer
     */
    public Customer createCustomer(CustomerDtoIn customerDtoIn) {

        usernameValidator.validate(customerDtoIn.getUsername());
        passwordValidator.validate(customerDtoIn.getPassword());
        Customer customer = CustomerMapper.toInBound(customerDtoIn);
        customer.setPassword(passwordEncoder.encode(customerDtoIn.getPassword()));


        List<Role> roles = new ArrayList<>();
        roleRepository.findById(2).ifPresent(roles::add);
        customer.setCustomerRoles(roles);
        customerRepository.save(customer);

        return customer;

    }

    /**
     * returns the customer info to the view
     *
     * @param username the customer is found by the username
     * @return the Customer
     */
    public Customer getProfile(String username) {

        return customerRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("user with the username was not found", username));
    }

    /**
     * deletes a customer from db, requires adm role
     *
     * @param customerId the customer is found using the id
     */
    public void delete(Integer customerId) {

        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new UserNotFoundException("user with the id was not found", customerId));
        customerRepository.delete(customer);

    }
}
