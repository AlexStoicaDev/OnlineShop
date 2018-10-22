package ro.msg.learning.shop.mappers;

import lombok.experimental.UtilityClass;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.repositories.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;


@UtilityClass
public class OrderDetailMapper {


    public OrderDetail toInBound(OrderDetailDto orderDetailDto, ProductRepository productRepository) {
        OrderDetail orderDetail = new OrderDetail();
        //add set Order? mb
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


    public List<OrderDetail> listToInBound(List<OrderDetailDto> orderDetailDtos, ProductRepository productRepository) {
        return orderDetailDtos.parallelStream().
            map(orderDetailDto -> OrderDetailMapper.
                toInBound(orderDetailDto, productRepository)).
            collect(Collectors.toList());

    }

    public List<OrderDetailDto> listToOutBound(List<OrderDetail> orderDetails) {
        return orderDetails.parallelStream().
            map(OrderDetailMapper::toOutBound).
            collect(Collectors.toList());

    }
}
