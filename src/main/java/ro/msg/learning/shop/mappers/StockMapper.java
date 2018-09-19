package ro.msg.learning.shop.mappers;
import lombok.experimental.UtilityClass;
import ro.msg.learning.shop.dtos.StockDto;
import ro.msg.learning.shop.entities.Stock;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class StockMapper {
    public  StockDto toOutBound(Stock stock) {
        StockDto stockDto=new StockDto();
        stockDto.setLocationId(stock.getLocation().getId());
        stockDto.setProductId(stock.getProduct().getId());
        stockDto.setQuantity(stock.getQuantity());
        return stockDto;
    }

    public  List<StockDto> listToOutBound(List<Stock> stocks){
        return stocks.parallelStream().
            map(StockMapper::toOutBound).
            collect(Collectors.toList());
    }
}
