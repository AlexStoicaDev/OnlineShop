package ro.msg.learning.shop.controllers;


import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.msg.learning.shop.services.OrderDetailService;

@RequestMapping("/orderdetail")
@RestController
@RequiredArgsConstructor
public class OrderDetailController {

    @Autowired
    public OrderDetailController(OrderDetailService orderDetailService) {
    }

//    @GetMapping("/create")
//    public OrderDetail doSome(@RequestBody OrderDetailDto orderDetailDto)
//    {
//        return orderDetailService.create(orderDetailDto);
//    }
}
