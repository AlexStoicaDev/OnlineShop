package ro.msg.learning.shop.services;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.strategies.LocationStrategy;
import ro.msg.learning.shop.wrappers.StockQuantityWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StockServiceTest {

    @Spy
    private StockRepository stockRepository;
    @Mock
    private LocationStrategy locationStrategy;


    private StockService stockService;

    @Before
    public void setUp() {
        stockService = new StockService(stockRepository, locationStrategy);
    }


    @Test
    public void reduceStockQuantityTest() {
        Stock stock = new Stock();
        stock.setId(-1);

        when(stockRepository.save(stock))
            .thenReturn(stock);

        when(stockRepository.findById(stock.getId()))
            .thenReturn(Optional.of(stock));

        stock.setQuantity(10);
        stockRepository.save(stock);

        StockQuantityWrapper stockQuantityWrapper = new StockQuantityWrapper(stock, 5);
        stockService.reduceStockQuantity(stockQuantityWrapper);


        assertEquals("Stock quantity", stockRepository.findById(stock.getId()).get().getQuantity().intValue(), 5);
        stockRepository.delete(stock);
    }

    @Test
    public void getStocksForLocationTest() {

        List<Stock> stocks = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Stock stock = new Stock();
            stock.setId(i);
            stock.setQuantity(i);
            stocks.add(stock);
        }
        when(stockRepository.findAllByLocation(any())).thenReturn(stocks);
        assertEquals("List of stocks", stocks, stockService.getStocksForLocation(5));

    }

    @Test
    public void fromCsvFileTest() {

    }

}
