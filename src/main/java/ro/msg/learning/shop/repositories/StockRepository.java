package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;


public interface StockRepository extends JpaRepository<Stock,Integer> {
    Stock findByLocationAndProduct(Location location, Product product);

    //optional
    Stock findByProductAndQuantityGreaterThanEqual(Product product,Integer quantity);
}
