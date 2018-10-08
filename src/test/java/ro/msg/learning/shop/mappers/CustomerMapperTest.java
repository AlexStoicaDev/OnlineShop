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

        final val customerDtoOut = CustomerMapper.toOutBound(customer);
        assertEquals("First Name:", customer.getFirstName(), customerDtoOut.getFirstName());
        assertEquals("Last Name:", customer.getLastName(), customerDtoOut.getLastName());
        assertEquals("Username:", customer.getUsername(), customerDtoOut.getUsername());

    }

    //!!!! password was up bro?
    @Test
    public void toInBoundTest() {
        final val customerDtoIn = new CustomerDtoIn();
        customerDtoIn.setFirstName("First name");
        customerDtoIn.setLastName("Last name");
        customerDtoIn.setPassword("admin");
        customerDtoIn.setUsername("admin");
        final val customer = CustomerMapper.toInBound(customerDtoIn);

        assertEquals("First name:", customerDtoIn.getFirstName(), customer.getFirstName());
        assertEquals("Last name:", customerDtoIn.getLastName(), customer.getLastName());
       /* assertEquals("Password:\n" +
            "$2a$10$UTdnj4KtVvhGR6p08XFqr.IDh5fZkAUrtRCcdFsoa4KzSWmEAy7V.\n",
            new BCryptPasswordEncoder().encode(customerDtoIn.getPassword()),customer.getPassword());*/
        assertEquals("Username:", customerDtoIn.getUsername(), customer.getUsername());
    }

}
