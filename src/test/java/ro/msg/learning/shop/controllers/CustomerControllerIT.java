package ro.msg.learning.shop.controllers;

import lombok.val;
import org.flywaydb.core.Flyway;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import ro.msg.learning.shop.dtos.customers.CustomerDtoIn;
import ro.msg.learning.shop.dtos.customers.CustomerDtoOut;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerIT {
    @LocalServerPort
    private int randomServerPort;
    private String resourcePath;

    private OAuth2RestTemplate oAuth2RestTemplate;
    private TestRestTemplate restTemplate = null;
    private HttpHeaders headers = null;

    @Autowired
    private Flyway flyway;


    @After
    public void resetDB() {
        flyway.clean();
        flyway.migrate();
    }

    @Before
    public void setUp() {
        resourcePath = "http://localhost:";
        resourcePath += randomServerPort;
        restTemplate = new TestRestTemplate();

        headers = new HttpHeaders();
        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setPassword("admin");
        resourceDetails.setUsername("admin");
        resourceDetails.setAccessTokenUri(resourcePath + "/oauth/token");
        resourceDetails.setClientId("my-trusted-client");
        resourceDetails.setScope(asList("read", "write", "trust"));
        resourceDetails.setClientSecret("secret");
        resourceDetails.setGrantType("password");

        DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();

        oAuth2RestTemplate = new OAuth2RestTemplate(resourceDetails, clientContext);


    }


    @Test
    public void createTest() {

        CustomerDtoIn customerDtoIn = new CustomerDtoIn("firstName", "lastName", "username", "$2a$10$UTdnj4KtVvhGR6p08XFqr.IDh5fZkAUrtRCcdFsoa4KzSWmEAy7V.");
        HttpEntity<CustomerDtoIn> httpEntity = new HttpEntity<>(customerDtoIn, headers);
        ResponseEntity<CustomerDtoOut> result = restTemplate.postForEntity(resourcePath + "/api/customer", httpEntity, CustomerDtoOut.class);

        CustomerDtoOut customerDtoOut = result.getBody();
        assertEquals("Response status code", HttpStatus.CREATED.value(), result.getStatusCode().value());
        assertEquals("First name:", customerDtoIn.getFirstName(), customerDtoOut.getFirstName());
        assertEquals("Last name:", customerDtoIn.getLastName(), customerDtoOut.getLastName());
        assertEquals("Username:", customerDtoIn.getUsername(), customerDtoOut.getUsername());

    }


    @Test
    public void profileTestWithNoAuthorization() {

        ResponseEntity<CustomerDtoOut> result = restTemplate.getForEntity(resourcePath + "/api/customer", CustomerDtoOut.class);
        assertEquals("Response status code", HttpStatus.UNAUTHORIZED.value(), result.getStatusCode().value());
    }

    @Test
    public void profileTestWithAuthorization() {

        ResponseEntity<CustomerDtoOut> result = oAuth2RestTemplate.getForEntity(resourcePath + "/api/customer", CustomerDtoOut.class);

        val customerDtoOut = result.getBody();

        assertEquals("Response status code", HttpStatus.OK.value(), result.getStatusCode().value());
        assertEquals("First name", "admin", customerDtoOut.getFirstName());
        assertEquals("Last name", "admin", customerDtoOut.getLastName());
        assertEquals("Username", "admin", customerDtoOut.getUsername());

    }

    @Test
    public void deleteTestWithNoAuthorization() {

        CustomerDtoIn customerDtoIn = new CustomerDtoIn();
        customerDtoIn.setUsername("cbushe110");
        HttpEntity<CustomerDtoIn> httpEntity = new HttpEntity<>(customerDtoIn, new HttpHeaders());

        try {
            restTemplate.exchange(resourcePath + "/api/customer/1", HttpMethod.DELETE, httpEntity, CustomerDtoOut.class);
        } catch (ResourceAccessException ex) {

        }

    }

    @Test
    public void deleteTestWithBasicRole() {
        CustomerDtoIn customerDtoIn = new CustomerDtoIn();
        customerDtoIn.setUsername("cbushe110");
        HttpEntity<CustomerDtoIn> httpEntity = new HttpEntity<>(customerDtoIn, new HttpHeaders());

        try {
            oAuth2RestTemplate.exchange(resourcePath + "/api/customer/1", HttpMethod.DELETE, httpEntity, CustomerDtoOut.class);
        } catch (HttpClientErrorException ex) {

        }

    }

}
