package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Stock;

import java.util.List;


public interface LocationRepository  extends JpaRepository<Location,Integer> {

    Location findLocationByStocks(List<Stock> stocks);
}
