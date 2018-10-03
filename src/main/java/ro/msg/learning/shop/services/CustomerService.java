package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.Customers.CustomerDtoIn;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.mappers.CustomerMapper;
import ro.msg.learning.shop.repositories.CustomerRepository;
import ro.msg.learning.shop.repositories.RoleRepository;


@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;

    public Customer createCustomer(CustomerDtoIn customerDtoIn) {

        //passwordValidator.validate(customerDtoIn.getPassword());
        // usernameValidator.validate(customerDtoIn.getUserName());
        Customer customer = CustomerMapper.toInBound(customerDtoIn, roleRepository);
        customerRepository.save(customer);
        return customer;

    }
}
