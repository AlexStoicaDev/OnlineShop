package ro.msg.learning.shop.mappers;

import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.entities.Order;

public class OrderMapper {

    private OrderMapper() {
        super();
    }

    private static Order toInBound(OrderDto orderDto) {
        Order order = new Order();
        order.setAddress(orderDto.getAddress());
        order.setCustomer(orderDto.getCustomer());
        return order;
    }

    private static OrderDto toOutBound(Order order) {
        OrderDto orderDto=new OrderDto();
        orderDto.setAddress(order.getAddress());
        orderDto.setCustomer(order.getCustomer());

        return orderDto;
    }
}
