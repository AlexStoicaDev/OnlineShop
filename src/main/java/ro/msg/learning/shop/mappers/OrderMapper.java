package ro.msg.learning.shop.mappers;

import lombok.experimental.UtilityClass;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.repositories.CustomerRepository;
import ro.msg.learning.shop.repositories.ProductRepository;

@UtilityClass
public class OrderMapper {

    public OrderDto toOutBound(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setAddress(order.getAddress());
        orderDto.setCustomerId(order.getCustomer().getId());
        orderDto.setOrderDetails(OrderDetailMapper.listToOutBound(order.getOrderDetails()));
        orderDto.setOrderDate(order.getOrderDate());
        return orderDto;
    }

    public Order toInBound(OrderDto orderDto, ProductRepository productRepository, CustomerRepository customerRepository) {
        Order order = new Order();
        order.setOrderDetails(OrderDetailMapper.listToInBound(orderDto.getOrderDetails(), productRepository));
        order.setCustomer(customerRepository.getOne(orderDto.getCustomerId()));
        order.setAddress(orderDto.getAddress());
        // order.setLocations(locations);
        order.setOrderDate(orderDto.getOrderDate());
        return order;
    }

}
