package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.strategies.LocationStrategy;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationStrategy locationStrategy;

    public List<Location> getLocationsForOrder(List<OrderDetailDto> orderDetails) {

        return orderDetails.parallelStream().
            map(orderDetail -> locationStrategy.
                getStockForProduct(orderDetail).
                getLocation()).collect(Collectors.toList());

    }
}
