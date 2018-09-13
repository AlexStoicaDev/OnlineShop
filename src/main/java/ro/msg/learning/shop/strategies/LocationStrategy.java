package ro.msg.learning.shop.strategies;


import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.OrderDetail;

public interface LocationStrategy {
    Location getLocationForProduct(OrderDetail orderDetail);
}

