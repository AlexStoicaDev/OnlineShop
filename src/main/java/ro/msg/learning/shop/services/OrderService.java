package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.orders.OrderDtoIn;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.mappers.OrderMapper;
import ro.msg.learning.shop.repositories.OrderRepository;
import ro.msg.learning.shop.repositories.ProductRepository;

/*
 *  the application logic, regarding the Order entity
 */
@Service
@RequiredArgsConstructor
public class OrderService {


    private final OrderRepository orderRepository;
    private final LocationService locationService;
    private final StockService stockService;
    private final ProductRepository productRepository;
    private final OrderDetailsService orderDetailsService;


    /**
     * creates a new order for the customer
     *
     * @param orderDto order info received from the view
     * @param customer the order belongs to this customer
     * @return the new Order
     */
    public Order createOrder(OrderDtoIn orderDto, Customer customer) {

        orderDetailsService.validateOrderDetailsDto(orderDto.getOrderDetails());
        stockService.reduceStockQuantityForAllProductsFromOrder(orderDto);
        Order order = OrderMapper.toInBound(orderDto, productRepository);
        order.setLocations(locationService.getLocationsForOrder(orderDto));
        order.setCustomer(customer);
        orderRepository.save(order);

        orderDetailsService.saveOrderDetailsInDb(order);

        customer.getOrders().add(order);


        return order;

    }

}
