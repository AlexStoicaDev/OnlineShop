package ro.msg.learning.shop.wrappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"productId", "localDateTime"})
public class DateProductIdQuantityTotalRevenueWrapper {
    private LocalDateTime localDateTime;
    private Integer productId;
    private Integer quantity;
    private Integer totalRevenue;


}
