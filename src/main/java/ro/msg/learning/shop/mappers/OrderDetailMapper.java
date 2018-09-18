package ro.msg.learning.shop.mappers;

import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.repositories.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

public class OrderDetailMapper {
    private OrderDetailMapper() {
        super();
    }

    public static   OrderDetail toInBound(OrderDetailDto orderDetailDto, ProductRepository productRepository) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProduct(productRepository.getOne(orderDetailDto.getProductId()));
        orderDetail.setQuantity(orderDetailDto.getQuantity());
        return orderDetail;
    }

    public  static List<OrderDetail> listToInBound(List<OrderDetailDto> orderDetailDtos,ProductRepository productRepository){
        return orderDetailDtos.parallelStream().
            map(orderDetailDto -> OrderDetailMapper.
                toInBound((OrderDetailDto) orderDetailDto,productRepository)).
            collect(Collectors.toList());

    }
}
