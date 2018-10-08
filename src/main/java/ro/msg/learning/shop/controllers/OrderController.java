package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.mappers.OrderMapper;
import ro.msg.learning.shop.services.CustomerService;
import ro.msg.learning.shop.services.OrderService;

@RequestMapping("/order")
@RestController
@RequiredArgsConstructor

/**
 * responsible for controlling the application logic, regarding the Order entity
 */
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;

    /**
     * creates a new Order
     *
     * @param orderDto the new order gets all the data from this Dto
     * @return the new ORDER back to the view
     */
    @PostMapping(path = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(HttpEntity<OrderDto> httpEntity) {

        Customer customer = customerService.getProfile(httpEntity.getHeaders().get("authorization").get(0));
        return OrderMapper.toOutBound(orderService.createOrder(httpEntity.getBody(), customer));

    }

}
