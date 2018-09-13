package ro.msg.learning.shop.services;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.repositories.OrderRepository;
import ro.msg.learning.shop.strategies.LocationStrategy;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final LocationStrategy locationStrategy;
    private final StockService stockService;


    @Autowired
    public OrderService(OrderRepository orderRepository, LocationStrategy locationStrategy, StockService stockService) {
        this.orderRepository = orderRepository;
        this.locationStrategy = locationStrategy;
        this.stockService = stockService;
    }


    public String doSome() {
        return locationStrategy.toString();
    }


    public Order createOrder(OrderDto orderDto) {
        Order order = new Order();
        List<Location> locations = new ArrayList<>();
        List<StrategyWrapper> locationQuantityProductList =
            this.getLocationProductQuantityListForOrder(orderDto.getOrderDetails());


        for(StrategyWrapper strategyWrapper:locationQuantityProductList){
            locations.add(strategyWrapper.getLocation());
            stockService.reduceStockQuantity(strategyWrapper.getLocation(),
                strategyWrapper.getProduct(),strategyWrapper.getQuantity());
        }

        order.setOrderDetails(orderDto.getOrderDetails());
        order.setCustomer(orderDto.getCustomer());
        order.setAddress(orderDto.getAddress());
        order.setLocations(locations);
        orderRepository.save(order);

        return order;

    }

    private List<StrategyWrapper> getLocationProductQuantityListForOrder(List<OrderDetail> orderDetails) {


        List<StrategyWrapper> locationQuantityProductList = new ArrayList<>();

        for (OrderDetail orderDetailTemp : orderDetails) {
            locationQuantityProductList.add(new StrategyWrapper(orderDetailTemp.getProduct(), (locationStrategy.getLocationForProduct
                            (orderDetailTemp)), orderDetailTemp.getQuantity()));
        }
        return locationQuantityProductList;
    }

    @Data
    @AllArgsConstructor
    private static class StrategyWrapper {
        private Product product;
        private Location location;
        private int quantity;
    }



}
