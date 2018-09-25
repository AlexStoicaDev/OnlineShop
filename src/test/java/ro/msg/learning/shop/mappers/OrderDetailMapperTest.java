package ro.msg.learning.shop.mappers;

import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailMapperTest {


    @Mock
    private ProductRepository productRepository;


    private Product product = new Product();

    @Before
    public void before() {

        product.setId(1);
        when(productRepository.getOne(any())).thenReturn(product);
    }

    @Test
    public void testToInBound() {

        /* Given */


        val orderDetailDto = new OrderDetailDto();
        orderDetailDto.setProductId(product.getId());
        orderDetailDto.setQuantity(10);

        /* When */

        val orderDetail = OrderDetailMapper.toInBound(orderDetailDto, productRepository);

        /* Then */
        assertEquals("Product ID", orderDetailDto.getProductId(), orderDetail.getProduct().getId().intValue());
        assertEquals("Product Quantity", orderDetailDto.getQuantity(), orderDetail.getQuantity());
    }

    @Test
    public void testToOutBound() {

        /* Given */
        val orderDetail = new OrderDetail();

        product.setId(1);
        orderDetail.setProduct(product);
        orderDetail.setQuantity(10);

        /* When */

        val orderDetailDto = OrderDetailMapper.toOutBound(orderDetail);

        /* Then */
        assertEquals("Product ID", product.getId().intValue(), orderDetailDto.getProductId());
        assertEquals("Product Quantity", orderDetail.getQuantity(), orderDetailDto.getQuantity());
    }

    @Test
    public void listToOutBound() {

        /* Given */
        List<OrderDetail> orderDetails = new ArrayList<>();
        val orderDetail1 = new OrderDetail();
        val orderDetail2 = new OrderDetail();
        val orderDetail3 = new OrderDetail();

        val quantity = 10;

        orderDetail1.setProduct(product);
        orderDetail1.setQuantity(quantity);

        orderDetail2.setProduct(product);
        orderDetail2.setQuantity(quantity);

        orderDetail3.setProduct(product);
        orderDetail3.setQuantity(quantity);

        orderDetails.add(orderDetail1);
        orderDetails.add(orderDetail2);
        orderDetails.add(orderDetail3);



        /* Then */
        OrderDetailMapper.listToOutBound(orderDetails).parallelStream().peek(orderDetailDto -> {
            assertEquals("Product Id ", product.getId().intValue(), orderDetailDto.getProductId());
            assertEquals("Quantity ", quantity, orderDetailDto.getQuantity().intValue());
        });


    }

    @Test
    public void listToInBound() {

        List<OrderDetailDto> orderDetails = new ArrayList<>();
        val orderDetail1 = new OrderDetailDto();
        val orderDetail2 = new OrderDetailDto();
        val orderDetail3 = new OrderDetailDto();

        val quantity = 10;

        orderDetail1.setProductId(product.getId());
        orderDetail1.setQuantity(quantity);

        orderDetail2.setProductId(product.getId());
        orderDetail2.setQuantity(quantity);

        orderDetail3.setProductId(product.getId());
        orderDetail3.setQuantity(quantity);

        orderDetails.add(orderDetail1);
        orderDetails.add(orderDetail2);
        orderDetails.add(orderDetail3);


        /* Then */
        OrderDetailMapper.listToInBound(orderDetails, productRepository).parallelStream().peek(orderDetail -> {
            assertEquals("Product Id ", product.getId().intValue(), orderDetail.getProduct().getId().intValue());
            assertEquals("Quantity ", quantity, orderDetail.getQuantity().intValue());
        });
    }
}
