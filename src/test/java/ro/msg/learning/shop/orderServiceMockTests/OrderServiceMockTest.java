package ro.msg.learning.shop.orderServiceMockTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.repositories.CustomerRepository;
import ro.msg.learning.shop.repositories.OrderRepository;
import ro.msg.learning.shop.repositories.ProductRepository;
import ro.msg.learning.shop.services.LocationService;
import ro.msg.learning.shop.services.OrderDetailsService;
import ro.msg.learning.shop.services.OrderService;
import ro.msg.learning.shop.services.StockService;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class OrderServiceMockTest {

    @Mock
    CustomerRepository customerRepository;
    OrderRepository orderRepository;
    LocationService locationService;
    StockService stockService;
    ProductRepository productRepository;
    OrderDetailsService orderDetailsService;

    @Mock
    OrderDto orderDto;

    @InjectMocks
    OrderService orderService;


    @Test
    public void testCreateOrder() {

        // when(stockService.reduceStockQuantityForAllProductsFromOrder(orderDto.getOrderDetails()));

    }


}
