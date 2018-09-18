package ro.msg.learning.shop.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.msg.learning.shop.entities.Product;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto {

    private Product product;
    private Integer quantity;

}
