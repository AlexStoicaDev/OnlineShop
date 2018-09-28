package ro.msg.learning.shop.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.entities.embeddables.Address;

import java.io.IOException;
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
    private OrderDto orderDto = null;
    private TestRestTemplate restTemplate = null;
    private HttpHeaders headers = null;

    @Before
    public void setUp() {
        resourcePath = "http://localhost:" + randomServerPort;

        restTemplate = new TestRestTemplate();
        headers = new HttpHeaders();

        orderDto = new OrderDto();
        orderDto.setCustomerId(1);
        List<OrderDetailDto> orderDetails = new ArrayList<>();
        orderDetails.add(new OrderDetailDto(1, 5));
        orderDetails.add(new OrderDetailDto(5, 6));
        orderDetails.add(new OrderDetailDto(4, 5));
        orderDto.setAddress(new Address("Romania", "Timisoara", "Banat", "Gg Lazar"));
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setOrderDetails(orderDetails);
    }


    @Test
    public void createOrderTest() throws IOException {

        HttpEntity<OrderDto> httpEntity = new HttpEntity<>(orderDto, headers);
        ResponseEntity<OrderDto> result = restTemplate.postForEntity(resourcePath + "/order/create", httpEntity, OrderDto.class);
        OrderDto resultOrderDto = result.getBody();
        assertEquals("Response status code", result.getStatusCode().value(), HttpStatus.CREATED.value());
        assertEquals("Customer Id", orderDto.getCustomerId(), resultOrderDto.getCustomerId());
        assertEquals("Order details", orderDto.getOrderDetails(), resultOrderDto.getOrderDetails());
        assertEquals("Address", orderDto.getAddress(), resultOrderDto.getAddress());
        assertEquals("Order date", orderDto.getOrderDate(), resultOrderDto.getOrderDate());
    }
}


