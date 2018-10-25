package ro.msg.learning.shop.strategies;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ro.msg.learning.shop.dtos.orders.OrderDtoIn;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.exceptions.ProductNotFoundException;
import ro.msg.learning.shop.exceptions.StockNotFoundException;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.ProductRepository;
import ro.msg.learning.shop.wrappers.StockQuantityProductWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class SingleLocationStrategy implements LocationStrategy {

    private final LocationRepository locationRepository;
    private final ProductRepository productRepository;

    /*
    returns a list of StockQuantityWrappers used for order creation and reducing the stock for each product in the order
     */
    @Override
    public List<StockQuantityProductWrapper> getStockQuantityProductWrapper(OrderDtoIn orderDtoIn) {


        /*
        map that contains the products and the quantity required in the order for each product
         */
        Map<Product, Integer> productQuantityMap = new HashMap<>();
        orderDtoIn.getOrderDetails().forEach(orderDetailDto ->
            productQuantityMap.put(productRepository.
                    findById(orderDetailDto.getProductId()).orElseThrow(() -> new ProductNotFoundException("Product with the id was not found", orderDetailDto.getProductId()))
                , orderDetailDto.getQuantity())

        );


        //finds the Location that has all the products required in the order

        List<Location> locations = locationRepository.findAll();
        Location locationWithAllTheStocks = null;
        for (Location location : locations) {
            int nrOfStocks = 0;
            for (Stock stock : location.getStocks()) {
                if (productQuantityMap.containsKey(stock.getProduct()) && productQuantityMap.get(stock.getProduct()) <= stock.getQuantity()) {
                    nrOfStocks++;
                }
            }
            if (nrOfStocks == productQuantityMap.size()) {
                locationWithAllTheStocks = location;
                break;
            }

        }

        if (locationWithAllTheStocks != null) {
            return locationWithAllTheStocks.getStocks().parallelStream().map(stock ->
                new StockQuantityProductWrapper(stock, productQuantityMap.get(stock.getProduct()), stock.getProduct().getId())).collect(Collectors.toList());
        }

        log.error("No single location that contains all the required stocks found for order {}", orderDtoIn);
        throw new StockNotFoundException("No stocks found for products", orderDtoIn.getOrderDetails());
    }
}
