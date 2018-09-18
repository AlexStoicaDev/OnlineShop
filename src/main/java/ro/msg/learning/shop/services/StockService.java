package ro.msg.learning.shop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.repositories.StockRepository;

@Service
public class StockService {

    private final
    StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void reduceStockQuantity(Stock stock,Integer quantity){

        stock.setQuantity(stock.getQuantity()-quantity);
        stockRepository.save(stock);
    }
}
