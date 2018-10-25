package ro.msg.learning.shop.mappers;

import lombok.experimental.UtilityClass;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.repositories.ProductRepository;


@UtilityClass
public class OrderDetailMapper {


    public OrderDetail toInBound(OrderDetailDto orderDetailDto, ProductRepository productRepository) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProduct(productRepository.getOne(orderDetailDto.getProductId()));
        orderDetail.setQuantity(orderDetailDto.getQuantity());
        return orderDetail;
    }


    public OrderDetailDto toOutBound(OrderDetail orderDetail) {
        OrderDetailDto orderDetailDto = new OrderDetailDto();
        orderDetailDto.setProductId(orderDetail.getProduct().getId());
        orderDetailDto.setQuantity(orderDetail.getQuantity());
        return orderDetailDto;
    }

}
