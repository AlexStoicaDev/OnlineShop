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
import ro.msg.learning.shop.wrappers.StockQuantityWrapper;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final LocationStrategy locationStrategy;
    private final StockService stockService;
    private final ProductRepository productRepository;

    public Order createOrder(OrderDto orderDto) {

        this.reduceStockQuantityForAllProductFromOrder(this.getStockAndQuantityListForOrder(orderDto.getOrderDetails()));

        Order order=OrderMapper.toInBound(orderDto,productRepository,customerRepository);
        order.setLocations(this.getLocationsForOrder(orderDto.getOrderDetails()));

        orderRepository.save(order);
        return order ;

    }

    //need to save orderdetails in db!!





    private List<Location> getLocationsForOrder(List<OrderDetailDto> orderDetails){

    return  orderDetails.parallelStream().
        map(orderDetail -> locationStrategy.
            getStockForProduct(orderDetail).
            getLocation()).collect(Collectors.toList());

    }


    private void reduceStockQuantityForAllProductFromOrder(List<StockQuantityWrapper> stockQuantityWrappers){
        stockQuantityWrappers.parallelStream().forEach(stockService::reduceStockQuantity);
    }

    private List<StockQuantityWrapper> getStockAndQuantityListForOrder(List<OrderDetailDto> orderDetails) {

        return orderDetails.parallelStream()
            .map(orderDetailDto ->
                new StockQuantityWrapper(locationStrategy.
                    getStockForProduct(orderDetailDto),orderDetailDto.
                    getQuantity())).collect(Collectors.toList());

    }
}
