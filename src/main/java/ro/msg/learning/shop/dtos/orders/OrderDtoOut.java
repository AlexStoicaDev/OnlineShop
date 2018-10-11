package ro.msg.learning.shop.dtos.orders;

import lombok.Data;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.embeddables.Address;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDtoOut {
    private int customerId;
    private List<OrderDetailDto> orderDetails;
    private Address address;
    private LocalDateTime orderDate;
    private List<Address> locationNames;
}
