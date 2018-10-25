package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.orders.OrderDtoIn;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.strategies.LocationStrategy;

import java.util.List;
import java.util.stream.Collectors;

/**
 *  the application logic, regarding the Location entity
 */
@Service
@RequiredArgsConstructor
public class LocationService {


    private final LocationStrategy locationStrategy;


    /**
     * @param orderDtoIn contains the order info, including the products and their quantities
     * @return all the locations from where the products from the order will be taken
     */
    public List<Location> getLocationsForOrder(OrderDtoIn orderDtoIn) {

        return locationStrategy.getStockQuantityProductWrapper(orderDtoIn).
            parallelStream().map(stockQuantityProductWrapper -> stockQuantityProductWrapper.getStock().getLocation()).collect(Collectors.toList());
    }

}
