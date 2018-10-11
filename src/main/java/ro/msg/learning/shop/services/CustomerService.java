package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.customers.CustomerDtoIn;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.entities.Role;
import ro.msg.learning.shop.exceptions.UserNotFoundException;
import ro.msg.learning.shop.mappers.CustomerMapper;
import ro.msg.learning.shop.repositories.CustomerRepository;
import ro.msg.learning.shop.repositories.OrderDetailRepository;
import ro.msg.learning.shop.repositories.OrderRepository;
import ro.msg.learning.shop.repositories.RoleRepository;
import ro.msg.learning.shop.validators.PasswordValidator;
import ro.msg.learning.shop.validators.UsernameValidator;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final UsernameValidator usernameValidator;
    private final PasswordValidator passwordValidator;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public Customer createCustomer(CustomerDtoIn customerDtoIn) {

        usernameValidator.validate(customerDtoIn.getUsername());
        // passwordValidator.validate(customerDtoIn.getPassword());
        Customer customer = CustomerMapper.toInBound(customerDtoIn);

        List<Role> roles = new ArrayList<>();
        roleRepository.findById(2).ifPresent(roles::add);
        customer.setCustomerRoles(roles);
        customerRepository.save(customer);

        return customer;

    }

    public Customer getProfile(String username) {

        return customerRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("dasda", "daada"));
    }

    public void delete(CustomerDtoIn customerDtoIn) {

        Customer customer = customerRepository.findByUsername(customerDtoIn.getUsername()).orElseThrow(() -> new UserNotFoundException("aaa", "aaa"));
        customerRepository.delete(customer);

    }
}
