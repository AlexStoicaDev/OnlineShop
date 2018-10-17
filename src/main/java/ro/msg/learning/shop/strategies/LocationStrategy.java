package ro.msg.learning.shop.strategies;

import ro.msg.learning.shop.dtos.orders.OrderDtoIn;
import ro.msg.learning.shop.wrappers.StockQuantityProductWrapper;

import java.util.List;


public interface LocationStrategy {
    List<StockQuantityProductWrapper> getStockQuantityProductWrapper(OrderDtoIn orderDtoIn);
}

