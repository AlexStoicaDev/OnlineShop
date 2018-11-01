package ro.msg.learning.shop.mappers;

import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.dtos.customers.CustomerDtoIn;
import ro.msg.learning.shop.entities.Customer;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerMapperTest {


    @Test
    public void toOutBoundTest() {
        Customer customer = new Customer();
        customer.setFirstName("FirstName");
        customer.setLastName("LastName");
        customer.setUsername("Username");

        val customerDtoOut = CustomerMapper.toOutBound(customer);
        assertEquals("First Name:", customer.getFirstName(), customerDtoOut.getFirstName());
        assertEquals("Last Name:", customer.getLastName(), customerDtoOut.getLastName());
        assertEquals("Username:", customer.getUsername(), customerDtoOut.getUsername());

    }

    @Test
    public void toInBoundTest() {
        val customerDtoIn = new CustomerDtoIn();
        customerDtoIn.setFirstName("First name");
        customerDtoIn.setLastName("Last name");
        customerDtoIn.setPassword("admin");
        customerDtoIn.setUsername("admin");
        val customer = CustomerMapper.toInBound(customerDtoIn);

        assertEquals("First name:", customerDtoIn.getFirstName(), customer.getFirstName());
        assertEquals("Last name:", customerDtoIn.getLastName(), customer.getLastName());
        assertEquals("Username:", customerDtoIn.getUsername(), customer.getUsername());
    }

}
