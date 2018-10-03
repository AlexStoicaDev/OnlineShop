package ro.msg.learning.shop.converters;


import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.dtos.StockDto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CsvConverterTest {


    @Test
    @SneakyThrows
    public void fromCsvTest() {

        String s = "productId,locationId,quantity" + "\n" + "1,2,3" + "\n" + "1,2,3";
        InputStream targetStream = new ByteArrayInputStream(s.getBytes());
        CsvConverter.fromCsv(StockDto.class, targetStream).parallelStream().
            forEach(stockDto -> {
                assertEquals("Product id", 1, stockDto.getProductId().intValue());
                assertEquals("Location id", 2, stockDto.getLocationId().intValue());
                assertEquals("Quantity id", 3, stockDto.getQuantity().intValue());
            });

    }

    @Test
    @SneakyThrows
    public void toCsvTest() {
        List<StockDto> stockDtos = new ArrayList<>();
        stockDtos.add(new StockDto(1, 2, 3));
        stockDtos.add(new StockDto(1, 2, 3));

        OutputStream outputStream = new ByteArrayOutputStream();
        CsvConverter.toCsv(StockDto.class, stockDtos, outputStream);

        InputStream inputStream = new ByteArrayInputStream(((ByteArrayOutputStream) outputStream).toByteArray());

        CsvConverter.fromCsv(StockDto.class, inputStream).parallelStream().forEach(stockDto -> {
            assertEquals("Product id", 1, stockDto.getProductId().intValue());
            assertEquals("Location id", 2, stockDto.getLocationId().intValue());
            assertEquals("Quantity id", 3, stockDto.getQuantity().intValue());
        });


    }
}
