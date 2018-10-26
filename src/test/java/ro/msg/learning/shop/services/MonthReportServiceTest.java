package ro.msg.learning.shop.services;


import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.repositories.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MonthReportServiceTest {
    @Mock
    private OrderRepository orderRepository;
    private LocalDateTime orderDate;

    @Before
    public void before() {
        orderDate = LocalDateTime.now();
        Order order = new Order();
        Product product = new Product();
        OrderDetail orderDetail = new OrderDetail();

        product.setPrice(BigDecimal.valueOf(5));
        product.setId(1);
        product.setName("product");

        orderDetail.setProduct(product);
        orderDetail.setQuantity(5);
        orderDetail.setId(1);

        order.setOrderDetails(Collections.singletonList(orderDetail));
        order.setId(1);
        order.setOrderDate(orderDate);


        doAnswer(invocation -> Collections.singletonList(order)).when(orderRepository).findAllByOrderDateAfterAndOrderDateBefore(any(), any());

    }

    @Test
    public void getMonthReportInfoTest() {
        final val dateProductIdQuantityTotalRevenueWrapper =
            new MonthReportService(orderRepository).getMonthReportInfo(orderDate.minusMonths(1), orderDate.plusMonths(1)).get(0);
        assertEquals("Order date:", orderDate, dateProductIdQuantityTotalRevenueWrapper.getLocalDateTime());
        assertEquals("Product Id:", 1, dateProductIdQuantityTotalRevenueWrapper.getProductId().intValue());
        assertEquals("Quantity:", 5, dateProductIdQuantityTotalRevenueWrapper.getQuantity().intValue());
        assertEquals("Total Revenue:", 25, dateProductIdQuantityTotalRevenueWrapper.getTotalRevenue().intValue());

    }

}
