package ro.msg.learning.shop.controllers;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.mappers.OrderMapper;
import ro.msg.learning.shop.services.OrderService;

@RequestMapping("/order")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;



    @PostMapping("/create")
    public OrderDto create(@RequestBody OrderDto orderDto)
    {
        //is this ok?
      /*->*/  return OrderMapper.toOutBound(orderService.createOrder(orderDto));
    }

}
