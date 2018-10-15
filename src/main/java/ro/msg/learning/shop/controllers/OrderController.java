package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dtos.DistanceMatrixDto;
import ro.msg.learning.shop.dtos.orders.OrderDtoIn;
import ro.msg.learning.shop.dtos.orders.OrderDtoOut;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.mappers.OrderMapper;
import ro.msg.learning.shop.services.CustomerService;
import ro.msg.learning.shop.services.OrderService;
import ro.msg.learning.shop.strategies.ShortestLocationPathStrategy;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<String> test(@RequestBody OrderDtoIn orderDtoIn) {
        List<Stock> stocks = shortestLocationPathStrategy.test(orderDtoIn.getOrderDetails().get(0), orderDtoIn.getAddress());
        Collections.reverse(stocks);
        final val collect = stocks.stream().map(stock -> stock.getLocation().getAddress().getCity() + "->" + stock.getQuantity()).collect(Collectors.toList());
        collect.add(orderDtoIn.getAddress().getCity());
        return collect;
    }

    @PostMapping(path = "/matrix")
    public DistanceMatrixDto matrix(@RequestBody OrderDtoIn orderDtoIn) {
        return shortestLocationPathStrategy.distanceMatrix(orderDtoIn.getOrderDetails().get(0), orderDtoIn.getAddress());
    }

}
