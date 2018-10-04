package ro.msg.learning.shop.mappers;

import lombok.experimental.UtilityClass;
import ro.msg.learning.shop.dtos.Customers.CustomerDtoIn;
import ro.msg.learning.shop.dtos.Customers.CustomerDtoOut;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.repositories.RoleRepository;


@UtilityClass
public class CustomerMapper {

    public CustomerDtoOut toOutBound(Customer customer) {
      /*  return new CustomerDtoIn(customer.getFirstName(), customer.getLastName(), customer.getUsername(), "should not show password",
            customer.getCustomerRoles().parallelStream().map(Role::getId).collect(Collectors.toList()));*/
        return new CustomerDtoOut(customer.getFirstName(), customer.getLastName(), customer.getUsername());
    }

    public Customer toInBound(CustomerDtoIn customerDtoIn, RoleRepository roleRepository) {

        Customer customer = new Customer();

        customer.setFirstName(customerDtoIn.getFirstName());
        customer.setLastName(customerDtoIn.getLastName());
        customer.setUsername(customerDtoIn.getUsername());
        customer.setPassword(customerDtoIn.getPassword());

       /* List<Role> roleList = new ArrayList<>();
        customerDtoIn.getRoleIds().parallelStream().map(roleRepository::findById).forEach(optional -> optional.ifPresent(roleList::add));*/


        return customer;
    }
}
