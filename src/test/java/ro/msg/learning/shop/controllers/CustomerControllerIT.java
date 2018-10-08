package ro.msg.learning.shop.controllers;

import org.flywaydb.core.Flyway;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.dtos.customers.CustomerDtoIn;
import ro.msg.learning.shop.dtos.customers.CustomerDtoOut;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerIT {
    @LocalServerPort
    private int randomServerPort;
    private String resourcePath;

    private TestRestTemplate restTemplate = null;
    private HttpHeaders headers = null;

    @Autowired
    private Flyway flyway;

    @Mock
    private
    HttpServletRequest request;


    @After
    public void resetDB() {
        flyway.clean();
        flyway.migrate();
    }

    @Before
    public void setUp() {
        resourcePath = "http://localhost:" + randomServerPort;
        restTemplate = new TestRestTemplate();
        headers = new HttpHeaders();
        headers.add("Authorization", "Basic YWRtaW46YWRtaW4=");

    }


    @Test
    public void createTest() {

        CustomerDtoIn customerDtoIn = new CustomerDtoIn("firstName", "lastName", "username", "admin");
        HttpEntity<CustomerDtoIn> httpEntity = new HttpEntity<>(customerDtoIn, headers);
        ResponseEntity<CustomerDtoOut> result = restTemplate.postForEntity(resourcePath + "/customer/user", httpEntity, CustomerDtoOut.class);

        CustomerDtoOut customerDtoOut = result.getBody();
        assertEquals("Response status code", result.getStatusCode().value(), HttpStatus.CREATED.value());
        assertEquals("Customer first name:", customerDtoIn.getFirstName(), customerDtoOut.getFirstName());
        assertEquals("Customer last name:", customerDtoIn.getLastName(), customerDtoOut.getLastName());
        assertEquals("Customer username:", customerDtoIn.getUsername(), customerDtoOut.getUsername());

    }


    @Test
    public void profileTestWithAuthorization() {

        when(request.getHeader("Authorization")).thenReturn("Basic YWRtaW46YWRtaW4=");

        CustomerDtoOut customerDtoOut1 = new CustomerDtoOut();
        customerDtoOut1.setFirstName("admin");
        customerDtoOut1.setLastName("admin");
        customerDtoOut1.setUsername("admin");
        HttpEntity<CustomerDtoOut> httpEntity = new HttpEntity<>(customerDtoOut1, headers);


        ResponseEntity<CustomerDtoOut> result = restTemplate.getForEntity(resourcePath + "customer/profile", CustomerDtoOut.class, httpEntity);

        CustomerDtoOut customerDtoOut = result.getBody();

        assertEquals("First name", "admin", customerDtoOut.getFirstName());
        assertEquals("Last name", "admin", customerDtoOut.getLastName());
        assertEquals("Username", "admin", customerDtoOut.getUsername());

    }
}
