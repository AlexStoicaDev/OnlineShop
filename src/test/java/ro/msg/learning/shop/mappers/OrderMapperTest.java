package ro.msg.learning.shop.mappers;

import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.dtos.orders.OrderDtoIn;
import ro.msg.learning.shop.entities.*;
import ro.msg.learning.shop.entities.embeddables.Address;
import ro.msg.learning.shop.repositories.CustomerRepository;
import ro.msg.learning.shop.repositories.ProductRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMapperTest {


    @Mock
    private ProductRepository productRepository;
    @Mock
    private CustomerRepository customerRepository;

    private Order order;
    private Integer quantity;
    private Product product;

    private void orderMaker() {
        order = new Order();

        val orderDetails = new ArrayList<OrderDetail>();

        Customer customer;
        order.setOrderDate(LocalDateTime.now());
        order.setAddress(new Address("Romania",
            "Timisoara"
            , "Timis"
            , "Gh Lazar"));

        customer = new Customer();
        customer.setId(1);

        order.setCustomer(customer);

        product = new Product();
        product.setId(2);


        product.setName("aaaaa");


        OrderDetail orderDetail = new OrderDetail();
        OrderDetail orderDetail2 = new OrderDetail();
        OrderDetail orderDetail3 = new OrderDetail();

        quantity = 3;

        orderDetail.setQuantity(quantity);
        orderDetail.setProduct(product);

        orderDetail2.setQuantity(quantity);
        orderDetail2.setProduct(product);

        orderDetail3.setQuantity(quantity);
        orderDetail3.setProduct(product);

        orderDetails.add(orderDetail);
        orderDetails.add(orderDetail2);
        orderDetails.add(orderDetail3);

        order.setOrderDetails(orderDetails);

        List<Location> locations = new ArrayList<>();
        locations.add(new Location());
        order.setLocations(locations);
    }


    @Before
    public void before() {

        orderMaker();
        when(productRepository.getOne(any())).thenReturn(product);
        when(customerRepository.getOne(any())).thenReturn(order.getCustomer());

    }


    @Test
    public void testToOutBound() {


        val resultOrderDto = OrderMapper.toOutBound(order);
        assertEquals("Address:", order.getAddress(), resultOrderDto.getAddress());
        assertEquals("Order date:", order.getOrderDate(), resultOrderDto.getOrderDate());
        assertEquals("Customer Id:", order.getCustomer().getId().intValue(), resultOrderDto.getCustomerId());
        resultOrderDto.getOrderDetails().parallelStream().forEach(orderDetailDto -> {
            assertEquals("Product Id ", product.getId().intValue(), orderDetailDto.getProductId());
            assertEquals("Quantity ", quantity, orderDetailDto.getQuantity());
        });
    }

    @Test
    public void testToInBound() {


        OrderDtoIn orderDto = new OrderDtoIn(order.getOrderDetails().parallelStream().map(OrderDetailMapper::toOutBound).
            collect(Collectors.toList()), order.getAddress(), order.getOrderDate());
        val resultOrder = OrderMapper.toInBound(orderDto, productRepository);
        assertEquals("Address", orderDto.getAddress(), resultOrder.getAddress());
        assertEquals("Order date:", orderDto.getOrderDate(), resultOrder.getOrderDate());
        resultOrder.getOrderDetails().parallelStream().forEach(orderDetail -> {
            assertEquals("Product Id ", product.getId().intValue(), orderDetail.getProduct().getId().intValue());
            assertEquals("Quantity ", quantity, orderDetail.getQuantity());
        });


    }

}
