package ro.msg.learning.shop.wrappers;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class StockLocationQauntityWrapper {

    private List<StockQuantityProductWrapper> stockQuantityProductWrappers;
    private String locationName;
    private int locationId;
}
