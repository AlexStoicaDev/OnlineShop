package ro.msg.learning.shop.services;

import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.entities.*;
import ro.msg.learning.shop.repositories.OrderRepository;
import ro.msg.learning.shop.repositories.RevenueRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DailyRevenueServiceTest {


    private DailyRevenueService dailyRevenueService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private RevenueRepository revenueRepository;


    private List<Revenue> revenues = new ArrayList<>();

    private LocalDateTime localDateTime;

    @Before
    public void before() {

        localDateTime = LocalDateTime.now();

        Order order = new Order();
        Location location = new Location();
        location.setId(1);
        location.setRevenues(new ArrayList<>());

        OrderDetail orderDetail = new OrderDetail();
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(5));

        orderDetail.setQuantity(5);
        orderDetail.setProduct(product);
        orderDetail.setOrder(order);

        List<Order> orders = new ArrayList<>();
        orders.add(order);

        order.setOrderDetails(Collections.singletonList(orderDetail));
        order.setLocations(Collections.singletonList(location));
        location.setOrders(orders);

        Revenue revenue = new Revenue();
        revenue.setLocation(location);
        revenue.setDate(LocalDateTime.now());
        revenue.setSum(BigDecimal.valueOf(25));
        location.getRevenues().add(revenue);
        revenueRepository.save(revenue);

        
        doAnswer(invocation -> orders).when(orderRepository).findAllByOrderDateAfterAndOrderDateBefore(any(), any());
        doAnswer(invocation -> {
            revenues.add(invocation.getArgument(0));
            return invocation.getArgument(0);
        }).when(revenueRepository).save(any());

        dailyRevenueService = new DailyRevenueService(orderRepository, revenueRepository);
    }

    @Test
    public void createRevenueForADayTest() {

        dailyRevenueService.createRevenuesForADay(localDateTime);
        final val revenue = revenues.get(0);
        assertEquals("Location id:", 1, revenue.getLocation().getId().intValue());
        assertEquals("Sum", BigDecimal.valueOf(25), revenue.getSum());
    }
}
