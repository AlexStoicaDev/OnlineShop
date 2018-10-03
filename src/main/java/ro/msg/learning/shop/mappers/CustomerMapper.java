package ro.msg.learning.shop.mappers;

import lombok.experimental.UtilityClass;
import ro.msg.learning.shop.dtos.Customers.CustomerDtoIn;
import ro.msg.learning.shop.dtos.Customers.CustomerDtoOut;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.entities.Role;
import ro.msg.learning.shop.repositories.RoleRepository;

import java.util.ArrayList;
import java.util.List;


@UtilityClass
public class CustomerMapper {

    public CustomerDtoOut toOutBound(Customer customer) {
      /*  return new CustomerDtoIn(customer.getFirstName(), customer.getLastName(), customer.getUserName(), "should not show password",
            customer.getCustomerRoles().parallelStream().map(Role::getId).collect(Collectors.toList()));*/
        return new CustomerDtoOut(customer.getFirstName(), customer.getLastName(), customer.getUserName());
    }

    public Customer toInBound(CustomerDtoIn customerDtoIn, RoleRepository roleRepository) {

        Customer customer = new Customer();

        customer.setFirstName(customerDtoIn.getFirstName());
        customer.setLastName(customerDtoIn.getLastName());
        customer.setUserName(customerDtoIn.getUsername());
        customer.setPassword(customerDtoIn.getPassword());

       /* List<Role> roleList = new ArrayList<>();
        customerDtoIn.getRoleIds().parallelStream().map(roleRepository::findById).forEach(optional -> optional.ifPresent(roleList::add));*/

        List<Role> roles = new ArrayList<>();
        roleRepository.findById(2).ifPresent(role -> roles.add(role));
        customer.setCustomerRoles(roles);

        return customer;
    }
}
