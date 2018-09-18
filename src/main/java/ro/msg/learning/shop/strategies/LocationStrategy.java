package ro.msg.learning.shop.strategies;

import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.Stock;


public interface LocationStrategy {
    Stock getStockForProduct(OrderDetailDto orderDetailDto);
}

