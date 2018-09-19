package ro.msg.learning.shop.services;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.mappers.OrderMapper;
import ro.msg.learning.shop.repositories.CustomerRepository;
import ro.msg.learning.shop.repositories.OrderRepository;
import ro.msg.learning.shop.repositories.ProductRepository;
import ro.msg.learning.shop.strategies.LocationStrategy;
import ro.msg.learning.shop.wrappers.StrategyWrapper;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final LocationStrategy locationStrategy;
    private final StockService stockService;
    private final ProductRepository productRepository;

    public Order createOrder(OrderDto orderDto) {

        List<Location> locations = new ArrayList<>();
        List<StrategyWrapper> locationQuantityProductList =
            this.getLocationProductQuantityListForOrder(orderDto.getOrderDetails());

        for(StrategyWrapper strategyWrapper:locationQuantityProductList){
            locations.add(strategyWrapper.getStock().getLocation());
            stockService.reduceStockQuantity(strategyWrapper.getStock(),
               strategyWrapper.getQuantity());
        }
        Order order=OrderMapper.toInBound(orderDto,productRepository,customerRepository);
        //dto or no???
        order.setLocations(locations);
        orderRepository.save(order);
        return order ;

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
