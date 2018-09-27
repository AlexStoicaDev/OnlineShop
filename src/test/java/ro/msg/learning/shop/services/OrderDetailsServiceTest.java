package ro.msg.learning.shop.services;

import org.flywaydb.core.Flyway;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.exceptions.InvalidQuantityException;
import ro.msg.learning.shop.mappers.OrderDetailMapper;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class OrderDetailsServiceTest {


    @Autowired
    private Flyway flyway;

    @Autowired
    OrderDetailsService orderDetailsService;


    private List<OrderDetail> orderDetails;

    @After
    public void resetDB() {
        flyway.clean();
        flyway.migrate();
    }


    @Before
    public void setUp() {

        Order order = new Order();
        order.setId(1);

        Product product = new Product();
        product.setId(2);
        orderDetails = new ArrayList<>();
        int nrOfElements = 100;

        for (int i = 1; i < nrOfElements; i++) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(product);
            orderDetail.setQuantity(i);
            orderDetail.setId(i);
            orderDetails.add(orderDetail);

        }
        order.setOrderDetails(orderDetails);
    }


    @Test
    public void validateOrderDetailsDtoWhenQuantityIsValidTest() {
        orderDetailsService.validateOrderDetailsDto(OrderDetailMapper.listToOutBound(orderDetails));

    }

    @Test(expected = InvalidQuantityException.class)
    public void validateOrderDetailsDtoWhenQuantityIsNotValidTest() {

        orderDetails.get(1).setQuantity(-5);
        orderDetailsService.validateOrderDetailsDto(OrderDetailMapper.listToOutBound(orderDetails));

    }


}
