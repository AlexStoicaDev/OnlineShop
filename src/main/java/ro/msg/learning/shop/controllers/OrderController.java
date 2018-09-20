package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.dtos.TempClass;
import ro.msg.learning.shop.services.OrderService;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/order")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping(path = "/create")
    public List<TempClass> create(@RequestBody OrderDto orderDto) {
        TempClass tempClass1 = new TempClass("a", 1);
        TempClass tempClass2 = new TempClass("b", 2);
        List<TempClass> list = new ArrayList<>();
        list.add(tempClass1);
        list.add(tempClass2);

        return list;
    }

}
