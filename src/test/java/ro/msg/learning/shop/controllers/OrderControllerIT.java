package ro.msg.learning.shop.controllers;

import org.flywaydb.core.Flyway;
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
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.dtos.orders.OrderDtoIn;
import ro.msg.learning.shop.dtos.orders.OrderDtoOut;
import ro.msg.learning.shop.entities.embeddables.Address;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerIT {

    @LocalServerPort
    private int randomServerPort;
    private String resourcePath;
    private OrderDtoIn orderDto = null;
    private TestRestTemplate restTemplate = null;
    private HttpHeaders headers = null;
    private OAuth2RestTemplate oAuth2RestTemplate;

    @Autowired
    private Flyway flyway;

    //@After
    public void resetDB() {
        flyway.clean();
        flyway.migrate();
    }

    @Before
    public void setUp() {
        resourcePath = "http://localhost:" + randomServerPort;
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
    public void createTestWithNoAuth() {
        HttpEntity<OrderDtoIn> httpEntity = new HttpEntity<>(orderDto, headers);
        try {
            restTemplate.postForEntity(resourcePath + "/order/create", httpEntity, OrderDtoIn.class);
        } catch (ResourceAccessException ex) {

        }
    }

    @Test
    public void createTest() {
        orderDto = new OrderDtoIn();

        List<OrderDetailDto> orderDetails = new ArrayList<>();
        orderDetails.add(new OrderDetailDto(1, 1));
        orderDetails.add(new OrderDetailDto(5, 1));
        orderDetails.add(new OrderDetailDto(3, 1));
        orderDto.setAddress(new Address("Romania", "Timisoara", "Banat", "Gg Lazar"));
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setOrderDetails(orderDetails);


        HttpEntity<OrderDtoIn> httpEntity = new HttpEntity<>(orderDto, headers);

        ResponseEntity<OrderDtoOut> result = oAuth2RestTemplate.exchange(resourcePath + "/api/order", HttpMethod.POST, httpEntity, OrderDtoOut.class);
        OrderDtoOut resultOrderDto = result.getBody();

        assertEquals("Response status code", HttpStatus.CREATED.value(), result.getStatusCode().value());
        assertEquals("Customer Id", 9999, resultOrderDto.getCustomerId());
        assertEquals("Order details", orderDto.getOrderDetails(), resultOrderDto.getOrderDetails());
        assertEquals("Address", orderDto.getAddress(), resultOrderDto.getAddress());
        assertEquals("Order date", orderDto.getOrderDate(), resultOrderDto.getOrderDate());
        resetDB();
    }

    @Test
    public void createTestWhenQuantityIsInvalid() {
        orderDto = new OrderDtoIn();
        List<OrderDetailDto> orderDetails = new ArrayList<>();
        orderDetails.add(new OrderDetailDto(7, 5));
        orderDetails.add(new OrderDetailDto(5, -6));
        orderDetails.add(new OrderDetailDto(4, 5));
        orderDto.setAddress(new Address("Romania", "Timisoara", "Banat", "Gg Lazar"));
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setOrderDetails(orderDetails);

        HttpEntity<OrderDtoIn> httpEntity = new HttpEntity<>(orderDto, headers);
        try {
            oAuth2RestTemplate.postForEntity(resourcePath + "api/order", httpEntity, OrderDtoIn.class);
        } catch (HttpClientErrorException ex) {

        }

    }
}


