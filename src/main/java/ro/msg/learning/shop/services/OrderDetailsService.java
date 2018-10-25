package ro.msg.learning.shop.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.exceptions.InvalidQuantityException;
import ro.msg.learning.shop.repositories.OrderDetailRepository;

import java.util.List;


/*
 *  the application logic, regarding the OrderDetail entity
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderDetailsService {

    private final OrderDetailRepository orderDetailRepository;


    /**
     * saves the order details from order in db
     *
     * @param order contains the order details that will be saved
     */
    public void saveOrderDetailsInDb(Order order) {

        order.getOrderDetails().parallelStream()
            .peek(orderDetail -> orderDetail.setOrder(order))
            .forEach(orderDetailRepository::save);
    }

    /**
     * validates the order details, quantity of each product must be greater than 0
     *
     * @param orderDetails the orderDetails that will be validated
     */
    public void validateOrderDetailsDto(List<OrderDetailDto> orderDetails) {

        orderDetails.parallelStream()
            .filter(od -> od.getQuantity() < 1)
            .findFirst()
            .ifPresent(od -> {
                log.error("Quantity should be greater than 0 but is {} for product with the id {} for order detail{}", od.getQuantity(), od.getProductId(), od);
                throw new InvalidQuantityException("Bad quantity for product with the id: "
                    + od.getProductId(), od.getQuantity());
            });


    }
}
