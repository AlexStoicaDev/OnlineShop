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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


@RequestMapping("/stock")
@RestController
@RequiredArgsConstructor
/**
 *
 */
public class StockController {

    private final StockService stockService;


    /**
     * @param locationId
     * @return
     */
    @GetMapping(path = "/{locationId}", produces = "text/csv")
    public List<StockDto> getStocks(@PathVariable Integer locationId) {
        return StockMapper.listToOutBound(stockService.getStocksForLocation(locationId));
    }
    //do to file!

    @GetMapping(path = "/to-file/{locationId}", produces = "text/csv")
    public File getStocksToFile(@PathVariable Integer locationId) throws IOException {
        File file = new File("temp.csv");
        CsvConverter.toCsv(StockDto.class, StockMapper.listToOutBound(stockService.getStocksForLocation(locationId)), new FileOutputStream(file));
        return file;
    }
    /**
     * @param stockDtos
     * @return
     */
    @PostMapping(path = "/fromcsv", consumes = "text/csv")
    public List<StockDto> getStocksFromCsv(@RequestBody List<StockDto> stockDtos) {
        return stockDtos;
    }


    /**
     *
     * @param file
     * @return
     */
    @SneakyThrows
    @PostMapping(path = "/from-file")
    public List<StockDto> fromCsvFile(@RequestParam("file") MultipartFile file) {

        if (!file.getOriginalFilename().endsWith(".csv")) {

            throw new FileTypeMismatchException("Expected CSV file type but got " + file.getOriginalFilename());

        }
        return CsvConverter.fromCsv(StockDto.class, file.getInputStream());
    }
}
