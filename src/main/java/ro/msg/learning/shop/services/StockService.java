package ro.msg.learning.shop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Product;
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

    public void reduceStockQuantity(Location location, Integer productId,Integer quantity){
        Product product=new Product();
        product.setId(productId);

        Stock stock=stockRepository.findByLocationAndProduct(location,product);
        stock.setQuantity(stock.getQuantity()-quantity);
        stockRepository.save(stock);
    }
}
