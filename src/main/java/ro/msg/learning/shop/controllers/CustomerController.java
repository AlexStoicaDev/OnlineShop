package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dtos.Customers.CustomerDtoIn;
import ro.msg.learning.shop.dtos.Customers.CustomerDtoOut;
import ro.msg.learning.shop.mappers.CustomerMapper;
import ro.msg.learning.shop.repositories.CustomerRepository;
import ro.msg.learning.shop.services.CustomerService;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@RequestMapping("/customer")
@RestController
@RequiredArgsConstructor
public class CustomerController {


    private final CustomerRepository customerRepository;
    private final CustomerService customerService;


    //repository does not find any customer by username!!!!!!!!
    //
    @PostMapping(path = "/user")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDtoOut create(@RequestBody CustomerDtoIn customerDtoIn) {

        return CustomerMapper.toOutBound(customerService.createCustomer(customerDtoIn));

    }

    @GetMapping(path = "/profile")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDtoOut profile(HttpServletRequest request) throws Exception {


        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            // credentials = username:password
            return CustomerMapper.toOutBound(customerRepository.findByUsername((credentials.split(":", 2))[0]).get());
        }

        return null;

    }

    @DeleteMapping(path = "/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody CustomerDtoIn customerDtoIn) {

        customerRepository.delete(customerRepository.findByUsername(customerDtoIn.getUsername()).get());
    }


}
