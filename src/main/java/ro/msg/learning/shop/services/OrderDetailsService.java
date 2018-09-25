package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.exceptions.InvalidQuantityException;
import ro.msg.learning.shop.repositories.OrderDetailRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderDetailsService {

    private final OrderDetailRepository orderDetailRepository;

    public void createOrderDetails(Order order) {

        order.getOrderDetails().parallelStream()
            .peek(orderDetail -> orderDetail.setOrder(order))
            .forEach(orderDetailRepository::save);
    }


    public void validateOrderDetailsDto(List<OrderDetailDto> orderDetails) {

        orderDetails.parallelStream()
            .filter(od -> od.getQuantity() < 1)
            .findFirst()
            .ifPresent(od -> {
                throw new InvalidQuantityException("Bad quantity for product with the id: " + od.getProductId(), od.getQuantity());
            });


    }
}
