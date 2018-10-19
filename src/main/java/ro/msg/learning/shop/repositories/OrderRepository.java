package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.entities.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {


    List<Order> findAllByOrderDateAfterAndOrderDateBefore(LocalDateTime localDateTime1, LocalDateTime localDateTime2);

}
