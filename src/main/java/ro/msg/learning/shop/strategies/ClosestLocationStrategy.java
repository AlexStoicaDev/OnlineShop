package ro.msg.learning.shop.strategies;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.entities.embeddables.Address;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.utils.DistanceMatrixUtil;

import java.util.Map;


@RequiredArgsConstructor
public class ClosestLocationStrategy implements LocationStrategy {

    private final StockRepository stockRepository;
    private final RestTemplate restTemplate;
    private final String apiKey;

    @Override
    public Stock getStockForProduct(OrderDetailDto orderDetailDto, Address address) {
        Product product = new Product();
        product.setId(orderDetailDto.getProductId());
        Map<Stock, Integer> stockDistanceMap = DistanceMatrixUtil.getStockDistanceMap(stockRepository.findAllByProductAndQuantityGreaterThan(product, orderDetailDto.getQuantity()),
            address, apiKey, restTemplate
        );

        Map.Entry<Stock, Integer> min = null;
        for (Map.Entry<Stock, Integer> entry : stockDistanceMap.entrySet()) {
            if (min == null || min.getValue() > entry.getValue()) {
                min = entry;
            }
        }
        //throw stock not found exception
        return min.getKey();
    }

}
