package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;

import java.util.List;


public interface StockRepository extends JpaRepository<Stock, Integer> {

    Stock findByProductAndQuantityGreaterThanEqual(Product product, Integer quantity);

    Stock findByProductAndLocation(Product product, Location location);

    List<Stock> findAllByProductAndQuantityGreaterThan(Product product, Integer quantity);

    List<Stock> findAllByLocation(Location location);
}
