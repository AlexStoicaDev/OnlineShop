package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.mappers.OrderMapper;
import ro.msg.learning.shop.services.OrderService;

@RequestMapping("/order")
@RestController
@RequiredArgsConstructor

/**
 * responsible for controlling the application logic, regarding the Order entity
 */
public class OrderController {

    private final OrderService orderService;

    /**
     * creates a new Order
     *
     * @param orderDto the new order gets all the data from this Dto
     * @return the new ORDER back to the view
     */
    @PostMapping(path = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(@RequestBody OrderDto orderDto) {
        return OrderMapper.toOutBound(orderService.createOrder(orderDto));
    }

}
