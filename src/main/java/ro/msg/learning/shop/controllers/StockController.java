package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.msg.learning.shop.converters.CsvConverter;
import ro.msg.learning.shop.dtos.StockDto;
import ro.msg.learning.shop.exceptions.FileTypeMismatchException;
import ro.msg.learning.shop.mappers.StockMapper;
import ro.msg.learning.shop.services.StockService;

import java.util.List;


/*
 *responsible for controlling the application logic, regarding the Stock entity
 */
@RequestMapping("/api/stock")
@RestController
@RequiredArgsConstructor
@Slf4j
public class StockController {

    private final StockService stockService;


    /**
     * @param locationId location is found by this id
     * @return all the stocks for the location with the given id
     */
    @GetMapping(path = "/{locationId}", produces = "text/csv")
    public List<StockDto> getStocks(@PathVariable Integer locationId) {
        return StockMapper.listToOutBound(stockService.getStocksForLocation(locationId));
    }


    /**
     * @param stockDtos list of  stocks in CSV format from request body,that will be converted by the CSV converter
     * @return the converted list of stocks in Json format
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/fromcsv", consumes = "text/csv")
    public List<StockDto> getStocksFromCsv(@RequestBody List<StockDto> stockDtos) {
        return stockDtos;
    }


    /**
     * @param file file that contains  the list of stocks in CSV format, that will be converted from CSV by the converter
     * @return the converted list of stocks
     */

    @SneakyThrows
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/from-file")
    public List<StockDto> fromCsvFile(@RequestParam("file") MultipartFile file) {
        if (!file.getOriginalFilename().endsWith(".csv")) {

            log.error("Input file must be .csv but is {}", file.getOriginalFilename().split(".")[1]);
            throw new FileTypeMismatchException("Only CSV as input ", file.getOriginalFilename());
        }
        return CsvConverter.fromCsv(StockDto.class, file.getInputStream());
    }
}
