package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.mappers.OrderMapper;
import ro.msg.learning.shop.services.OrderService;
@RequestMapping("/order")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping(path = "/create")
    public OrderDto create(@RequestBody OrderDto orderDto) {


        return OrderMapper.toOutBound(orderService.createOrder(orderDto));
    }

}
