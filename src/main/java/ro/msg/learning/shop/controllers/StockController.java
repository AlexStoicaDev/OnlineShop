package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.dtos.StockDto;
import ro.msg.learning.shop.mappers.StockMapper;
import ro.msg.learning.shop.services.StockService;

import java.util.List;


@RequestMapping("/stock")
@RestController
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;


    //should return StocksDto
    @GetMapping(path = "/{locationId}", produces = "text/csv")
    public List<StockDto> getStocks(@PathVariable Integer locationId) {
        return StockMapper.listToOutBound(stockService.getStocksForLocation(locationId));
    }

}
