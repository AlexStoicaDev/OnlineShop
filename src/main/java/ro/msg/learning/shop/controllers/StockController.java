package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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
    @GetMapping("/{locationId}")
    public List<StockDto> getStocks(@PathVariable Integer locationId)
    {
        return StockMapper.listToOutBound(stockService.getStocksForLocation(locationId));
    }

}
