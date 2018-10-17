package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.orders.OrderDtoIn;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.strategies.LocationStrategy;
import ro.msg.learning.shop.wrappers.StockQuantityWrapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final LocationStrategy locationStrategy;

    public void reduceStockQuantity(StockQuantityWrapper stockQuantityWrapper) {

        stockQuantityWrapper.getStock().setQuantity(stockQuantityWrapper.getStock().getQuantity() - stockQuantityWrapper.getQuantity());
        stockRepository.save(stockQuantityWrapper.getStock());
    }

    public List<Stock> getStocksForLocation(Integer locationId) {
        Location location = new Location();
        location.setId(locationId);
        return stockRepository.findAllByLocation(location);

    }

    public void reduceStockQuantityForAllProductsFromOrder(OrderDtoIn orderDtoIn) {

        this.getStockAndQuantityListForOrder(orderDtoIn).parallelStream().forEach(this::reduceStockQuantity);
    }

    private List<StockQuantityWrapper> getStockAndQuantityListForOrder(OrderDtoIn orderDtoIn) {

        return locationStrategy.getStockQuantityProductWrapper(orderDtoIn).stream().map(stockQuantityProductWrapper ->
            new StockQuantityWrapper(stockQuantityProductWrapper.getStock(), stockQuantityProductWrapper.getQuantity())
        ).collect(Collectors.toList());

    }
}
