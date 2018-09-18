package ro.msg.learning.shop.strategies;


import lombok.AllArgsConstructor;
import lombok.val;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;


import ro.msg.learning.shop.repositories.StockRepository;




@AllArgsConstructor
public class SingleLocationStrategy implements LocationStrategy {

    private StockRepository stockRepository;


    //for next strategy should return a list of stocks not a single Stock!!!
    @Override
    public Stock getStockForProduct(OrderDetailDto orderDetailDto) {

        val product = new Product();
        product.setId(orderDetailDto.getProductId());

        return stockRepository.findByProductAndQuantityGreaterThanEqual(product, orderDetailDto.getQuantity());


    }
}
