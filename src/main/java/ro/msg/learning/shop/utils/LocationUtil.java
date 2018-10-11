package ro.msg.learning.shop.utils;

import lombok.experimental.UtilityClass;
import lombok.val;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.repositories.StockRepository;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class LocationUtil {

    public List<Location> getAllLocationsThatHaveProduct(OrderDetailDto orderDetailDto, StockRepository stockRepository) {

        final val product = new Product();
        product.setId(orderDetailDto.getProductId());
        return stockRepository.findAllByProductAndQuantityGreaterThan(product, orderDetailDto.getQuantity())
            .parallelStream().map(Stock::getLocation).collect(Collectors.toList());
    }
}
