package ro.msg.learning.shop.strategies;

import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.entities.embeddables.Address;


public interface LocationStrategy {
    Stock getStockForProduct(OrderDetailDto orderDetailDto, Address address);
}

