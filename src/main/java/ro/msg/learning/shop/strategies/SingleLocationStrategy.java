package ro.msg.learning.shop.strategies;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.entities.embeddables.Address;
import ro.msg.learning.shop.exceptions.StockNotFoundException;
import ro.msg.learning.shop.repositories.StockRepository;

@Slf4j
@RequiredArgsConstructor
public class SingleLocationStrategy implements LocationStrategy {

    private final StockRepository stockRepository;



    @Override
    public Stock getStockForProduct(OrderDetailDto orderDetailDto, Address address) {

        val product = new Product();
        product.setId(orderDetailDto.getProductId());

        final val stock = stockRepository.
            findByProductAndQuantityGreaterThanEqual(product, orderDetailDto.getQuantity());

        if (stock != null) {
            return stock;
        }

        log.error("No stocks found for product {}", product);
        throw new StockNotFoundException("No stocks found for product with the id " + product.getId(), null);
    }
}
