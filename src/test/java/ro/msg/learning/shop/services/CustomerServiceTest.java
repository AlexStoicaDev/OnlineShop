package ro.msg.learning.shop.services;

import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.dtos.customers.CustomerDtoIn;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.entities.Role;
import ro.msg.learning.shop.exceptions.UserNotFoundException;
import ro.msg.learning.shop.repositories.CustomerRepository;
import ro.msg.learning.shop.repositories.RoleRepository;
import ro.msg.learning.shop.validators.PasswordValidator;
import ro.msg.learning.shop.validators.UsernameValidator;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceTest {

    @Autowired
    private UsernameValidator usernameValidator;

    @Autowired
    private PasswordValidator passwordValidator;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private RoleRepository roleRepository;


    @Mock
    private CustomerService customerService;


    @Before
    public void before() {

        Customer customer = new Customer();
        customer.setFirstName("admin");
        customer.setLastName("admin");
        customer.setUsername("admin");

        doAnswer(invocation -> Optional.of(customer)).when(customerRepository).findByUsername("admin");
        doAnswer(invocation -> invocation.getArgument(0)).when(passwordEncoder).encode(any());
        doAnswer(invocation -> Optional.of(new Role())).when(roleRepository).findById(any());
        doAnswer(invocation -> invocation.getArgument(0)).when(customerRepository).save(any());
        customerService = new CustomerService(customerRepository, roleRepository, usernameValidator, passwordValidator, passwordEncoder);
    }



    @Test
    public void createCustomerTest() {

        val customerDtoIn = new CustomerDtoIn();
        customerDtoIn.setFirstName("First name");
        customerDtoIn.setLastName("Last name");
        customerDtoIn.setPassword("$2a$10$UTdnj4KtVvhGR6p08XFqr.IDh5fZkAUrtRCcdFsoa4KzSWmEAy7V.");
        customerDtoIn.setUsername("testUsername");


        val customer = customerService.createCustomer(customerDtoIn);

        assertEquals("First name:", customerDtoIn.getFirstName(), customer.getFirstName());
        assertEquals("Last name:", customerDtoIn.getLastName(), customer.getLastName());
        assertEquals("Username", customerDtoIn.getUsername(), customer.getUsername());
    }

    @Test
    public void getProfileTest() {


        val profile = customerService.getProfile("admin");
        assertEquals("First name", "admin", profile.getFirstName());
        assertEquals("Last name", "admin", profile.getLastName());
        assertEquals("Username", "admin", profile.getUsername());

    }

    @Test(expected = UserNotFoundException.class)
    public void getProfileWhenUsernameIsNotFromDbTest() {
        customerService.getProfile("badUsername");
    }
}
