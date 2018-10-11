package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.orders.OrderDtoIn;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.strategies.LocationStrategy;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {


    private final LocationStrategy locationStrategy;

    public List<Location> getLocationsForOrder(OrderDtoIn orderDtoIn) {

        return orderDtoIn.getOrderDetails().parallelStream().
            map(orderDetail -> locationStrategy.
                getStockForProduct(orderDetail, orderDtoIn.getAddress()).
                getLocation()).collect(Collectors.toList());

    }
}
