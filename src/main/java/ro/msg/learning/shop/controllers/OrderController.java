package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dtos.orders.OrderDtoIn;
import ro.msg.learning.shop.dtos.orders.OrderDtoOut;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.mappers.OrderMapper;
import ro.msg.learning.shop.services.CustomerService;
import ro.msg.learning.shop.services.MonthReportService;
import ro.msg.learning.shop.services.OrderService;
import ro.msg.learning.shop.writers.ExcelWriter;

import java.security.Principal;

@RequestMapping("/api/order")
@RestController
@RequiredArgsConstructor

/**
 * responsible for controlling the application logic, regarding the Order entity
 */
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final MonthReportService monthReportService;
    private final ExcelWriter excelWriter;

    /**
     * creates a new Order
     *
     * @return the new ORDER information back to the view
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDtoOut create(@RequestBody OrderDtoIn orderDtoIn, Principal principal) {

        Customer customer = customerService.getProfile(principal.getName());
        return OrderMapper.toOutBound(orderService.createOrder(orderDtoIn, customer));

    }


}
