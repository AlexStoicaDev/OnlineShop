package ro.msg.learning.shop.strategies;


import lombok.AllArgsConstructor;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.entities.Stock;

import ro.msg.learning.shop.repositories.StockRepository;




@AllArgsConstructor
public class SingleLocationStrategy implements LocationStrategy {

    private StockRepository stockRepository;



    @Override
    public Location getLocationForProduct(OrderDetail orderDetail) {

        Stock stock = stockRepository.findByProductAndQuantity(orderDetail.getProduct(), orderDetail.getQuantity());

        return  stock.getLocation();

    }
}
