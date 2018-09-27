package ro.msg.learning.shop.services;

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

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class OrderServiceTest {

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
