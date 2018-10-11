package ro.msg.learning.shop.services;

import lombok.val;
import org.flywaydb.core.Flyway;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.dtos.customers.CustomerDtoIn;
import ro.msg.learning.shop.exceptions.UserNotFoundException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceTest {


    @Autowired
    Flyway flyway;

    @After
    public void resetDB() {
        flyway.clean();
        flyway.migrate();
    }

    @Autowired
    CustomerService customerService;

    @Test
    public void createCustomerTest() {
        final val customerDtoIn = new CustomerDtoIn();
        customerDtoIn.setFirstName("First name");
        customerDtoIn.setLastName("Last name");
        customerDtoIn.setPassword("admin");
        customerDtoIn.setUsername("testUsername");

        final val customer = customerService.createCustomer(customerDtoIn);

        assertEquals("First name:", customerDtoIn.getFirstName(), customer.getFirstName());
        assertEquals("Last name:", customerDtoIn.getLastName(), customer.getLastName());
        assertEquals("Username", customerDtoIn.getUsername(), customer.getUsername());
    }

    @Test
    public void getProfileTest() {

        final val profile = customerService.getProfile("admin");
        assertEquals("First name", "admin", profile.getFirstName());
        assertEquals("Last name", "admin", profile.getLastName());
        assertEquals("Username", "admin", profile.getUsername());

    }

    @Test(expected = UserNotFoundException.class)
    public void getProfileWhenUsernameIsNotFromDbTest() {
        customerService.getProfile("aaaaaaaaa");
    }
}
