package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.entities.Product;


public interface ProductRepository extends JpaRepository<Product,Integer> {

}
