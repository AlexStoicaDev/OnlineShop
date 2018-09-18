package ro.msg.learning.shop.mappers;

import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.OrderDetail;

public class OrderDetailMapper {
    private OrderDetailMapper() {
        super();
    }

    public static   OrderDetail toInBound(OrderDetailDto orderDetailDto) {
        OrderDetail orderDetail = new OrderDetail();
       // orderDetail.setProduct(orderDetailDto.getProduct());
        orderDetail.setQuantity(orderDetailDto.getQuantity());
        return orderDetail;
    }

//    public static OrderDetailDto toOutBound(OrderDetail orderDetail) {
//       return new OrderDetailDto(orderDetail.getProduct(), orderDetail.getQuantity());
//    }
}
