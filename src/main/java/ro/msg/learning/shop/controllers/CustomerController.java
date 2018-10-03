package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dtos.Customers.CustomerDtoIn;
import ro.msg.learning.shop.dtos.Customers.CustomerDtoOut;
import ro.msg.learning.shop.mappers.CustomerMapper;
import ro.msg.learning.shop.services.CustomerService;

@Slf4j
@RequestMapping("/customer")
@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping(path = "/user")
    @ResponseStatus(HttpStatus.CREATED)


    public CustomerDtoOut create(@RequestBody CustomerDtoIn customerDtoIn) {

        return CustomerMapper.toOutBound(customerService.createCustomer(customerDtoIn));


    }
}
