package ro.msg.learning.shop.wrappers;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class QuantityTotalRevenueWrapper {
    private Integer quantity;
    private Integer totalRevenue;
}
