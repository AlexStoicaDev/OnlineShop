package ro.msg.learning.shop.strategies;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.entities.embeddables.Address;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.utils.DistanceMatrixUtil;

import java.util.Map;

@RequiredArgsConstructor
public class ShortestLocationPathStrategy implements LocationStrategy {

    private final StockRepository stockRepository;
    private final RestTemplate restTemplate;

    @Value("${online-shop.api-key}")
    private String apiKey;

    @Override
    public Stock getStockForProduct(OrderDetailDto orderDetailDto, Address address) {

        Product product = new Product();
        product.setId(orderDetailDto.getProductId());
        Map<Stock, Integer> stockDistanceMap = DistanceMatrixUtil.getStockDistanceMap(stockRepository.findAllByProductAndQuantityGreaterThan(product, orderDetailDto.getQuantity()),
            address, apiKey, restTemplate
        );


        return null;
    }


    public void createGraph(Stock[][] stocks) {

    }
}
