package ro.msg.learning.shop.strategies;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.dtos.orders.OrderDtoIn;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.exceptions.StockNotFoundException;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.ProductRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SingleLocationStrategyTest {


    @Mock
    private ProductRepository productRepository;
    @Mock
    private LocationRepository locationRepository;

    private OrderDetailDto orderDetailDto;
    private OrderDtoIn orderDtoIn;


    private SingleLocationStrategy singleLocationStrategy;


    @Before
    public void setUp() {


        Product product = new Product();
        product.setId(1);

        orderDetailDto = new OrderDetailDto();
        orderDetailDto.setProductId(1);

        orderDtoIn = new OrderDtoIn();
        orderDtoIn.setOrderDetails(Collections.singletonList(orderDetailDto));

        Location location = new Location();

        Stock stock = new Stock();
        stock.setLocation(location);
        stock.setQuantity(10);
        stock.setProduct(product);
        location.setStocks(Collections.singletonList(stock));


        when(locationRepository.findAll()).thenReturn(Collections.singletonList(location));
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        singleLocationStrategy = new SingleLocationStrategy(locationRepository, productRepository);
    }

    /*
     * should not throw ex
     */
    @Test
    public void getStockForProductWhenValid() {
        try {
            orderDetailDto.setQuantity(10);
            singleLocationStrategy.getStockQuantityProductWrapper(orderDtoIn);
        } catch (Exception e) {
            fail("Exception  thrown");
        }
    }

    /**
     * should throw ex, if quantity is too big and no stocks are found
     */
    @Test(expected = StockNotFoundException.class)
    public void getStockForProductWhenInValid() {
        orderDetailDto.setQuantity(1000000);
        singleLocationStrategy.getStockQuantityProductWrapper(orderDtoIn);
    }
}
