package ro.msg.learning.shop.mappers;

import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;

import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StockMapperTest {


    private Stock stockMaker() {

        Random r = new Random();
        Stock stock = new Stock();
        stock.setId(r.nextInt());

        Location location = new Location();
        location.setId(r.nextInt());

        Product product = new Product();
        product.setId(r.nextInt());

        stock.setQuantity(r.nextInt());
        stock.setLocation(location);
        stock.setProduct(product);

        return stock;

    }

    @Test
    public void testToOutBound() {
        val stock = stockMaker();
        val resultStockDto = StockMapper.toOutBound(stock);

        assertEquals("Product Id", stock.getProduct().getId(), resultStockDto.getProductId());
        assertEquals("Location Id", stock.getLocation().getId(), resultStockDto.getLocationId());
        assertEquals("Quantity", stock.getQuantity(), resultStockDto.getQuantity());

    }
    
}
