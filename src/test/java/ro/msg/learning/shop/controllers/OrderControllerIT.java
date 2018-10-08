package ro.msg.learning.shop.controllers;

import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResourceAccessException;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.dtos.orders.OrderDtoIn;
import ro.msg.learning.shop.dtos.orders.OrderDtoOut;
import ro.msg.learning.shop.entities.embeddables.Address;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    }


    @Test
    public void createOrderTestWithNoAuth() {
        HttpEntity<OrderDtoIn> httpEntity = new HttpEntity<>(orderDto, headers);
        try {
            restTemplate.postForEntity(resourcePath + "/order/create", httpEntity, OrderDtoIn.class);
        } catch (ResourceAccessException ex) {

        }
    }

    @Test
    public void createOrderTest() {
        orderDto = new OrderDtoIn();

        List<OrderDetailDto> orderDetails = new ArrayList<>();
        orderDetails.add(new OrderDetailDto(7, 5));
        orderDetails.add(new OrderDetailDto(5, 6));
        orderDetails.add(new OrderDetailDto(4, 5));
        orderDto.setAddress(new Address("Romania", "Timisoara", "Banat", "Gg Lazar"));
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setOrderDetails(orderDetails);

        HttpEntity<OrderDtoIn> httpEntity = new HttpEntity<>(orderDto, headers);
        ResponseEntity<OrderDtoOut> result = restTemplate.withBasicAuth("admin", "admin").postForEntity(resourcePath + "/order/create", httpEntity, OrderDtoOut.class);

        OrderDtoOut resultOrderDto = result.getBody();

        assertEquals("Response status code", HttpStatus.CREATED.value(), result.getStatusCode().value());
        assertEquals("Customer Id", 9999, resultOrderDto.getCustomerId());
        assertEquals("Order details", orderDto.getOrderDetails(), resultOrderDto.getOrderDetails());
        assertEquals("Address", orderDto.getAddress(), resultOrderDto.getAddress());
        assertEquals("Order date", orderDto.getOrderDate(), resultOrderDto.getOrderDate());
        resetDB();
    }

    @Test
    public void createOrderTestWhenQuantityIsInvalid() {
        orderDto = new OrderDtoIn();
        List<OrderDetailDto> orderDetails = new ArrayList<>();
        orderDetails.add(new OrderDetailDto(7, 5));
        orderDetails.add(new OrderDetailDto(5, -6));
        orderDetails.add(new OrderDetailDto(4, 5));
        orderDto.setAddress(new Address("Romania", "Timisoara", "Banat", "Gg Lazar"));
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setOrderDetails(orderDetails);

        HttpEntity<OrderDtoIn> httpEntity = new HttpEntity<>(orderDto, headers);
        ResponseEntity<OrderDtoIn> result = restTemplate.withBasicAuth("admin", "admin").postForEntity(resourcePath + "/order/create", httpEntity, OrderDtoIn.class);

        assertEquals("Response status code", HttpStatus.BAD_REQUEST.value(), result.getStatusCode().value());
    }
}


