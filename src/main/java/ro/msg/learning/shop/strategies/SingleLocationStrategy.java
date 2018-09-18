package ro.msg.learning.shop.strategies;


import lombok.AllArgsConstructor;
import lombok.val;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;

import ro.msg.learning.shop.repositories.StockRepository;




@AllArgsConstructor
public class SingleLocationStrategy implements LocationStrategy {

    private StockRepository stockRepository;



    @Override
    public Location getLocationForProduct(OrderDetailDto orderDetailDto) {

        val product = new Product();
        product.setId(orderDetailDto.getProductId());

        Stock stock = stockRepository.findByProductAndQuantityGreaterThanEqual(product, orderDetailDto.getQuantity());

        return  stock.getLocation();

    }
}
