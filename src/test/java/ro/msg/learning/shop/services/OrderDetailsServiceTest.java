package ro.msg.learning.shop.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.exceptions.InvalidQuantityException;
import ro.msg.learning.shop.mappers.OrderDetailMapper;
import ro.msg.learning.shop.repositories.OrderDetailRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.fail;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class OrderDetailsServiceTest {

    @Mock
    private OrderDetailRepository orderDetailRepository;

    private OrderDetailsService orderDetailsService;


    private List<OrderDetail> orderDetails;

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

        orderDetailsService = new OrderDetailsService(orderDetailRepository);
    }


    @Test
    public void validateOrderDetailsDtoWhenQuantityIsValidTest() {
        try {
            orderDetailsService.validateOrderDetailsDto(orderDetails.parallelStream().map(OrderDetailMapper::toOutBound).collect(Collectors.toList()));
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = InvalidQuantityException.class)
    public void validateOrderDetailsDtoWhenQuantityIsNotValidTest() {

        orderDetails.get(1).setQuantity(-5);
        orderDetailsService.validateOrderDetailsDto(orderDetails.parallelStream().map(OrderDetailMapper::toOutBound).collect(Collectors.toList()));

    }


}
