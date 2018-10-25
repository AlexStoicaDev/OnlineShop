package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dtos.customers.CustomerDtoIn;
import ro.msg.learning.shop.dtos.customers.CustomerDtoOut;
import ro.msg.learning.shop.mappers.CustomerMapper;
import ro.msg.learning.shop.services.CustomerService;

import javax.websocket.server.PathParam;
import java.security.Principal;

/*
 * responsible for controlling the application logic, regarding the Customer entity
 */
@Slf4j
@RequestMapping("/api/customer")
@RestController
@RequiredArgsConstructor
public class CustomerController {


    private final CustomerService customerService;


    /**
     * creates a new customer in db using customerDtoIn fields
     *
     * @return the new CUSTOMER information back to the view
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDtoOut create(@RequestBody CustomerDtoIn customerDtoIn) {

        return CustomerMapper.toOutBound(customerService.createCustomer(customerDtoIn));

    }

    /**
     * @return the profile of the logged customer
     */
    @GetMapping
    public CustomerDtoOut profile(Principal principal) {
        return CustomerMapper.toOutBound(customerService.getProfile(principal.getName()));

    }

    /**
     * deletes a customer from db
     * @param customerId the customer is found using it's id
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathParam("id") Integer customerId) {

        customerService.delete(customerId);


    }


}
