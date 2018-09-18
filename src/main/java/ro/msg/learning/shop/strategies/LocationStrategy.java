package ro.msg.learning.shop.strategies;


import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.Location;


public interface LocationStrategy {
    Location getLocationForProduct(OrderDetailDto orderDetailDto);
}
