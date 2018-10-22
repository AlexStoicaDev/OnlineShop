package ro.msg.learning.shop.mappers;

import lombok.experimental.UtilityClass;
import ro.msg.learning.shop.dtos.customers.CustomerDtoIn;
import ro.msg.learning.shop.dtos.customers.CustomerDtoOut;
import ro.msg.learning.shop.entities.Customer;


@UtilityClass
public class CustomerMapper {


    public CustomerDtoOut toOutBound(Customer customer) {

        return new CustomerDtoOut(customer.getFirstName(), customer.getLastName(), customer.getUsername());
    }

    public Customer toInBound(CustomerDtoIn customerDtoIn) {

        Customer customer = new Customer();

        customer.setFirstName(customerDtoIn.getFirstName());
        customer.setLastName(customerDtoIn.getLastName());
        customer.setUsername(customerDtoIn.getUsername());


        return customer;
    }

}
