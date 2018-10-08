package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.mappers.OrderMapper;
import ro.msg.learning.shop.repositories.CustomerRepository;
import ro.msg.learning.shop.repositories.OrderRepository;
import ro.msg.learning.shop.repositories.ProductRepository;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final LocationService locationService;
    private final StockService stockService;
    private final ProductRepository productRepository;
    private final OrderDetailsService orderDetailsService;


    public Order createOrder(OrderDto orderDto, Customer customer) {

        orderDetailsService.validateOrderDetailsDto(orderDto.getOrderDetails());
        stockService.reduceStockQuantityForAllProductsFromOrder(orderDto.getOrderDetails());
        Order order = OrderMapper.toInBound(orderDto, productRepository, customerRepository);
        order.setLocations(locationService.getLocationsForOrder(orderDto.getOrderDetails()));
        order.setCustomer(customer);
        orderRepository.save(order);

        orderDetailsService.createOrderDetails(order);

        customer.getOrders().add(order);


        return order;

    }
}
