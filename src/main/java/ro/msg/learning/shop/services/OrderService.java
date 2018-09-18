package ro.msg.learning.shop.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.mappers.OrderDetailMapper;
import ro.msg.learning.shop.repositories.CustomerRepository;
import ro.msg.learning.shop.repositories.OrderRepository;
import ro.msg.learning.shop.repositories.ProductRepository;
import ro.msg.learning.shop.strategies.LocationStrategy;
import ro.msg.learning.shop.wrappers.StrategyWrapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final LocationStrategy locationStrategy;
    private final StockService stockService;
    private final ProductRepository productRepository;


    @Autowired
    public OrderService(CustomerRepository customerRepository, OrderRepository orderRepository, LocationStrategy locationStrategy, StockService stockService, ProductRepository productRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.locationStrategy = locationStrategy;
        this.stockService = stockService;
        this.productRepository = productRepository;
    }

    public Order createOrder(OrderDto orderDto) {

        Order order = new Order();
        List<Location> locations = new ArrayList<>();
        List<StrategyWrapper> locationQuantityProductList =
            this.getLocationProductQuantityListForOrder(orderDto.getOrderDetails());


        for(StrategyWrapper strategyWrapper:locationQuantityProductList){
            locations.add(strategyWrapper.getStock().getLocation());
            stockService.reduceStockQuantity(strategyWrapper.getStock(),
               strategyWrapper.getQuantity());
        }


        order.setOrderDetails(OrderDetailMapper.listToInBound(orderDto.getOrderDetails(),productRepository));
        order.setCustomer(customerRepository.getOne(orderDto.getCustomerId()));
        order.setAddress(orderDto.getAddress());
        order.setLocations(locations);
        orderRepository.save(order);

        return order;

    }

    private List<StrategyWrapper> getLocationProductQuantityListForOrder(List<OrderDetailDto> orderDetails) {
        List<StrategyWrapper> stockQuantityProductList = new ArrayList<>();
        for (OrderDetailDto orderDetailDto : orderDetails) {
            stockQuantityProductList.add(new StrategyWrapper(orderDetailDto.getProductId(),
                locationStrategy.getStockForProduct(orderDetailDto),
                             orderDetailDto.getQuantity()));
        }
        return stockQuantityProductList;
    }

}
