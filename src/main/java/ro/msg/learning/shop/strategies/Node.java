package ro.msg.learning.shop.strategies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.msg.learning.shop.entities.Stock;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Node {
    private int locationId;
    private String locationName;
    private List<Stock> stocksFromOrder;
    private List<Integer> stockNumber;
}

