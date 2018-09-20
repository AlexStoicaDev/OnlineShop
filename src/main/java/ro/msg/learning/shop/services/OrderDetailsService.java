package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.repositories.OrderDetailRepository;


@Service
@RequiredArgsConstructor
public class OrderDetailsService {

    private final OrderDetailRepository orderDetailRepository;

    public void createOrderDetails(Order order) {

        order.getOrderDetails().parallelStream()
            .peek(orderDetail -> orderDetail.setOrder(order))
            .forEach(orderDetailRepository::save);
    }
}
