package ro.msg.learning.shop.services;

import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.dtos.orders.OrderDtoIn;
import ro.msg.learning.shop.entities.*;
import ro.msg.learning.shop.entities.embeddables.Address;
import ro.msg.learning.shop.mappers.OrderDetailMapper;
import ro.msg.learning.shop.repositories.OrderDetailRepository;
import ro.msg.learning.shop.repositories.OrderRepository;
import ro.msg.learning.shop.repositories.ProductRepository;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.strategies.LocationStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    private OrderService orderService;

    @Spy
    private OrderRepository orderRepository;
    @Mock
    private LocationService locationService;
    @Mock
    private StockService stockService;
    @Spy
    private ProductRepository productRepository;

    @Mock
    private OrderDetailsService orderDetailsService;

    @Spy
    private LocationStrategy locationStrategy;

    @Spy
    private StockRepository stockRepository;

    @Spy
    private OrderDetailRepository orderDetailRepository;

    private OrderDtoIn orderDto;
    private Customer customer;

    @Before
    public void setUp() {
        orderService = new OrderService(orderRepository,
            locationService, stockService, productRepository, orderDetailsService);

        orderDto = new OrderDtoIn();
        orderDto.setOrderDate(LocalDateTime.now());

        orderDto.setAddress(new Address("Romania", "Timisoara", "Timis", "Gh Lazar"));
        List<OrderDetailDto> orderDetails = new ArrayList<>();

        Product product = new Product();
        product.setId(1);

        customer = new Customer();
        customer.setId(1);

        customer.setOrders(new LinkedList<>());

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

        when(locationStrategy.getStockQuantityProductWrapper(any())).thenReturn(any());

        when(stockRepository.save(stock))
            .thenReturn(stock);
        when(productRepository.getOne(1)).
            thenReturn(product);

        when(orderRepository.save(any()))
            .thenReturn(new Order());

        when(orderDetailRepository.save(any())).thenReturn(new OrderDetail());

    }

    @Test
    public void createOrderTest() {

        val result = orderService.createOrder(orderDto, customer);
        assertEquals("Customer id", 1, result.getCustomer().getId().intValue());
        assertEquals("Oder date", orderDto.getOrderDate(), result.getOrderDate());
        assertEquals("Order Details", orderDto.getOrderDetails(), result.getOrderDetails().parallelStream().
            map(OrderDetailMapper::toOutBound).collect(Collectors.toList()));
        assertEquals("Address", orderDto.getAddress(), result.getAddress());
    }
}
