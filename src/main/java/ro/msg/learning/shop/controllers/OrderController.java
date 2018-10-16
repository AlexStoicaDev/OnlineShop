package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dtos.DistanceMatrixDto;
import ro.msg.learning.shop.dtos.orders.OrderDtoIn;
import ro.msg.learning.shop.dtos.orders.OrderDtoOut;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.mappers.OrderMapper;
import ro.msg.learning.shop.services.CustomerService;
import ro.msg.learning.shop.services.OrderService;
import ro.msg.learning.shop.strategies.ShortestLocationPathStrategy;
import ro.msg.learning.shop.wrappers.StockLocationQuantityWrapper;

import java.security.Principal;
import java.util.List;

@RequestMapping("/order")
@RestController
@RequiredArgsConstructor

/**
 * responsible for controlling the application logic, regarding the Order entity
 */
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final ShortestLocationPathStrategy shortestLocationPathStrategy;

    /**
     * creates a new Order
     *
     * @return the new ORDER back to the view
     */
    @PostMapping(path = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDtoOut create(@RequestBody OrderDtoIn orderDtoIn, Principal principal) {

        Customer customer = customerService.getProfile(principal.getName());
        return OrderMapper.toOutBound(orderService.createOrder(orderDtoIn, customer));

    }

    @PostMapping(path = "/testStrategy")
    public List<StockLocationQuantityWrapper> test(@RequestBody OrderDtoIn orderDtoIn) {
        return shortestLocationPathStrategy.test(orderDtoIn);

    }

    @PostMapping(path = "/matrix")
    public DistanceMatrixDto matrix(@RequestBody OrderDtoIn orderDtoIn) {
        return shortestLocationPathStrategy.distanceMatrix(orderDtoIn.getOrderDetails().get(0), orderDtoIn.getAddress());
    }

}
