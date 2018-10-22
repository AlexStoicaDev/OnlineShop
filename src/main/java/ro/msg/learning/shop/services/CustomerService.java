package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
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


/*
 *  the application logic, regarding the Customer entity
 */
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final UsernameValidator usernameValidator;
    private final PasswordValidator passwordValidator;
    private final PasswordService passwordService;

    public Customer createCustomer(CustomerDtoIn customerDtoIn) {

        usernameValidator.validate(customerDtoIn.getUsername());
        passwordValidator.validate(customerDtoIn.getPassword());
        Customer customer = CustomerMapper.toInBound(customerDtoIn);
        customer.setPassword(passwordService.hashPassword(customerDtoIn.getPassword()));


        List<Role> roles = new ArrayList<>();
        roleRepository.findById(2).ifPresent(roles::add);
        customer.setCustomerRoles(roles);
        customerRepository.save(customer);

        return customer;

    }

    public Customer getProfile(String username) {

        return customerRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("user with the username was not found", username));
    }

    public void delete(Integer customerId) {

        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new UserNotFoundException("user with the id was not found", customerId));
        customerRepository.delete(customer);

    }
}
