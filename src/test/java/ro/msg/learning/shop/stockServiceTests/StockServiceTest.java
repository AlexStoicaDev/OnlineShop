package ro.msg.learning.shop.stockServiceTests;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.services.StockService;
import ro.msg.learning.shop.wrappers.StockQuantityWrapper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class StockServiceTest {


    @InjectMocks
    StockService stockService;


    @Mock
    StockQuantityWrapper stockQuantityWrapper;

//    @Mock
//    Stock stock;

    @Test
    public void testReduceStockQuantity() {
        Stock stock = new Stock();
        stock.setQuantity(10);
        when(stockQuantityWrapper.getQuantity()).thenReturn(5);
        when(stockQuantityWrapper.getStock()).thenReturn(stock);
        stockService.reduceStockQuantity(stockQuantityWrapper);
        assertEquals(java.util.Optional.ofNullable(stock.getQuantity()), 5);
    }
}
