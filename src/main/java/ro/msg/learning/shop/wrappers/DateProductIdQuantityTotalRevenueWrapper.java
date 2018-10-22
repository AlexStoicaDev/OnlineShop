package ro.msg.learning.shop.wrappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateProductIdQuantityTotalRevenueWrapper {
    private String localDateTime;
    private String productId;
    private String quantity;
    private String totalRevenue;


}
