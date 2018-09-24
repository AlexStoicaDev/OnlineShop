package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.msg.learning.shop.converters.CsvConverter;
import ro.msg.learning.shop.dtos.StockDto;
import ro.msg.learning.shop.exceptions.FileTypeMismatchException;
import ro.msg.learning.shop.mappers.StockMapper;
import ro.msg.learning.shop.services.StockService;

import java.util.List;


@RequestMapping("/stock")
@RestController
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;


    @GetMapping(path = "/{locationId}", produces = "text/csv")
    public List<StockDto> getStocks(@PathVariable Integer locationId) {
        return StockMapper.listToOutBound(stockService.getStocksForLocation(locationId));
    }


    @PostMapping(path = "/fromcsv", consumes = "text/csv")
    public List<StockDto> getStocksFromCsv(@RequestBody List<StockDto> stockDtos) {
        return stockDtos;
    }


    @SneakyThrows
    @PostMapping(path = "/from-file")
    public List<StockDto> aksfdh(@RequestParam("file") MultipartFile file) {

        if (!file.getOriginalFilename().endsWith(".csv")) {
            throw new FileTypeMismatchException();

        }
        return CsvConverter.fromCsv(StockDto.class, file.getInputStream());
    }
}
