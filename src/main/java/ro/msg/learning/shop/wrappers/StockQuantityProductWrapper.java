package ro.msg.learning.shop.wrappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import ro.msg.learning.shop.entities.Stock;

@Data
@AllArgsConstructor
public class StockQuantityProductWrapper {
    private Stock stock;
    private int quantity;
    private int productId;
}
