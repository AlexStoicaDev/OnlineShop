package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.repositories.StockRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    public void reduceStockQuantity(Stock stock,Integer quantity){

        stock.setQuantity(stock.getQuantity()-quantity);
        stockRepository.save(stock);
    }
    public List<Stock> getStocksForLocation(Integer locationId){
        Location location=new Location();
        location.setId(locationId);
        return stockRepository.findAllByLocation(location);
    }
}
