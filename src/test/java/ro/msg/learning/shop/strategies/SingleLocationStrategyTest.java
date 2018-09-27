package ro.msg.learning.shop.strategies;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.exceptions.StockNotFoundException;
import ro.msg.learning.shop.repositories.ProductRepository;
import ro.msg.learning.shop.repositories.StockRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SingleLocationStrategyTest {

    //better name?

    @Autowired
    ProductRepository productRepository;
    @Autowired
    StockRepository stockRepository;

    private OrderDetailDto orderDetailDto;

    @Autowired
    private LocationStrategy singleLocationStrategy;

    @Before
    public void setUp() {

        orderDetailDto = new OrderDetailDto();
        orderDetailDto.setProductId(productRepository.findAll().get(0).getId());
    }

    /**
     * should not throw ex
     */
    @Test//better name pls help?
    public void getStockForProductWhenValid() {
        orderDetailDto.setQuantity(10);
        singleLocationStrategy.getStockForProduct(orderDetailDto);

    }

    /**
     * should throw ex, if quantity is too big and no stocks are found
     */
    @Test(expected = StockNotFoundException.class)
    public void getStockForProductWhenInValid() {
        orderDetailDto.setQuantity(1000000);
        singleLocationStrategy.getStockForProduct(orderDetailDto);
    }
}
