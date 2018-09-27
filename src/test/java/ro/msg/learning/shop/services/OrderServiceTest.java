package ro.msg.learning.shop.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.entities.embeddables.Address;
import ro.msg.learning.shop.mappers.OrderMapper;
import ro.msg.learning.shop.repositories.*;
import ro.msg.learning.shop.strategies.LocationStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    private OrderService orderService;

    @Spy
    private CustomerRepository customerRepository;
    @Spy
    private OrderRepository orderRepository;
    @Spy
    private LocationService locationService;
    @Spy
    private StockService stockService;
    @Spy
    private ProductRepository productRepository;
    @Spy
    private OrderDetailsService orderDetailsService;

    @Spy
    private LocationStrategy locationStrategy;

    @Spy
    private StockRepository stockRepository;

    @Spy
    private OrderDetailRepository orderDetailRepository;

    private OrderDto orderDto;

    @Before
    public void setUp() {
        orderService = new OrderService(customerRepository, orderRepository,
            locationService, stockService, productRepository, orderDetailsService);

        orderDto = new OrderDto();
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setCustomerId(1);
        orderDto.setAddress(new Address("Romania", "Timisoara", "Timis", "Gh Lazar"));
        List<OrderDetailDto> orderDetails = new ArrayList<>();

        Product product = new Product();
        product.setId(1);

        Customer customer = new Customer();
        customer.setId(1);

        for (int i = 1; i < 50; i++) {
            OrderDetailDto orderDetail = new OrderDetailDto();
            orderDetail.setQuantity(i);
            orderDetail.setProductId(product.getId());
            orderDetails.add(orderDetail);
        }

        orderDto.setOrderDetails(orderDetails);

        Stock stock = new Stock();
        stock.setId(1);
        Location location = new Location();
        location.setId(1);

        stock.setLocation(location);

        when(locationStrategy.getStockForProduct(any()))
            .thenReturn(stock);

        when(stockRepository.save(stock))
            .thenReturn(stock);

        when(customerRepository.getOne(1)).
            thenReturn(customer);
        when(productRepository.getOne(1)).
            thenReturn(product);

        when(orderRepository.save(any()))
            .thenReturn(any());

        when(orderDetailRepository.save(any())).thenReturn(any());

    }

    //what is this ??? bruh no reason for this test ????
    @Test
    public void createOrderTest() {

        OrderDto result = OrderMapper.toOutBound(orderService.createOrder(orderDto));
        assertEquals("OrderDto id", orderDto.getCustomerId(), orderDto.getCustomerId());


    }


}
