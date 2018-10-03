package ro.msg.learning.shop.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.msg.learning.shop.entities.embeddables.Address;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private int customerId;
    private List<OrderDetailDto> orderDetails;
    private Address address;
    private LocalDateTime orderDate;

}
