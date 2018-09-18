package ro.msg.learning.shop.controllers;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.services.OrderService;

@RequestMapping("/order")
@RestController
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;

    }

    @PostMapping("/create")
    public Order doSome(@RequestBody OrderDto orderDto)
    {
        return orderService.createOrder(orderDto);
    }

}
