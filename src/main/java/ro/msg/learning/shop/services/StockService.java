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

/**
 *  the application logic, regarding the Stock entity
 */
@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final LocationStrategy locationStrategy;

    /**
     * reduces a stock quantity
     *
     * @param stockQuantityWrapper contains the stock and the quantity that will be subtracted from the stocks quantity
     */
    public void reduceStockQuantity(StockQuantityWrapper stockQuantityWrapper) {

        stockQuantityWrapper.getStock().setQuantity(stockQuantityWrapper.getStock().getQuantity() - stockQuantityWrapper.getQuantity());
        stockRepository.save(stockQuantityWrapper.getStock());
    }

    /**
     * @param locationId the location is found is it's id
     * @return al the stocks from a location as a list
     */

    public List<Stock> getStocksForLocation(Integer locationId) {
        Location location = new Location();
        location.setId(locationId);
        return stockRepository.findAllByLocation(location);

    }

    /**
     * reduces all the stock quantities for all the products in order
     *
     * @param orderDtoIn contains the order  info, products and their quantities
     */
    public void reduceStockQuantityForAllProductsFromOrder(OrderDtoIn orderDtoIn) {

        this.getStockAndQuantityListForOrder(orderDtoIn).parallelStream().forEach(this::reduceStockQuantity);
    }

    /**
     * @param orderDtoIn contains the products and the quantity for each product
     * @return a list that contains the stocks that will be used for the order and the quantities that will be taken from each stock
     */
    private List<StockQuantityWrapper> getStockAndQuantityListForOrder(OrderDtoIn orderDtoIn) {

        return locationStrategy.getStockQuantityProductWrapper(orderDtoIn).parallelStream().map(stockQuantityProductWrapper ->
            new StockQuantityWrapper(stockQuantityProductWrapper.getStock(), stockQuantityProductWrapper.getQuantity())
        ).collect(Collectors.toList());

    }


}
