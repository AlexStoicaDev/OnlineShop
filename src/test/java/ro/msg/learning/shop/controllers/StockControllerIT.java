package ro.msg.learning.shop.controllers;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.converters.CsvConverter;
import ro.msg.learning.shop.dtos.StockDto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StockControllerIT {

    @LocalServerPort
    private int randomServerPort;
    private String resourcePath;
    private TestRestTemplate restTemplate = null;
    private HttpHeaders headers = null;

    private CsvConverter csvConverter;

    @Before
    public void setUp() {
        resourcePath = "http://localhost:" + randomServerPort;
        restTemplate = new TestRestTemplate();
        headers = new HttpHeaders();
        csvConverter = new CsvConverter();
        headers.setAccept(Collections.singletonList(new MediaType("text", "csv")));
    }

    @Test
    public void getStocksTest() throws IOException {

        ResponseEntity<String> response = restTemplate.getForEntity(
            resourcePath + "/stock/8",
            String.class);

        String result = response.getBody();

        val stockDtos = CsvConverter.fromCsv(StockDto.class, new ByteArrayInputStream(response.getBody().getBytes()));

        assertEquals("Response status code", HttpStatus.OK.value(), response.getStatusCode().value());

        StockDto stockDto1 = stockDtos.get(0);
        assertEquals("Product Id", 6, stockDto1.getProductId().intValue());
        assertEquals("Location Id", 8, stockDto1.getLocationId().intValue());
        assertEquals("Quantity", 1, stockDto1.getQuantity().intValue());

        stockDto1 = stockDtos.get(1);
        assertEquals("Product Id", 7, stockDto1.getProductId().intValue());
        assertEquals("Location Id", 8, stockDto1.getLocationId().intValue());
        assertEquals("Quantity", 36, stockDto1.getQuantity().intValue());

        stockDto1 = stockDtos.get(2);
        assertEquals("Product Id", 8, stockDto1.getProductId().intValue());
        assertEquals("Location Id", 8, stockDto1.getLocationId().intValue());
        assertEquals("Quantity", 32, stockDto1.getQuantity().intValue());

        stockDto1 = stockDtos.get(3);
        assertEquals("Product Id", 9, stockDto1.getProductId().intValue());
        assertEquals("Location Id", 8, stockDto1.getLocationId().intValue());
        assertEquals("Quantity", 24, stockDto1.getQuantity().intValue());

    }

    @Test
    @SuppressWarnings("unchecked")
    public void getStocksFromCsvTest() throws IOException {


        String s = "productId,locationId,quantity" + "\n" + "1,2,3" + "\n" + "1,2,3\n";

        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.valueOf("text/csv"));

        HttpEntity<String> httpEntity = new HttpEntity<>(s, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
            resourcePath + "/stock/fromcsv", httpEntity,
            String.class);


        List<StockDto> stockDtos = new Gson().fromJson(response.getBody(), new TypeToken<List<StockDto>>() {
        }.getType());
        assertEquals("Response status code", HttpStatus.CREATED.value(), response.getStatusCode().value());

        StockDto stockDto1 = stockDtos.get(0);
        assertEquals("Product Id", 1, stockDto1.getProductId().intValue());
        assertEquals("Location Id", 2, stockDto1.getLocationId().intValue());
        assertEquals("Quantity", 3, stockDto1.getQuantity().intValue());

        stockDto1 = stockDtos.get(1);
        assertEquals("Product Id", 1, stockDto1.getProductId().intValue());
        assertEquals("Location Id", 2, stockDto1.getLocationId().intValue());
        assertEquals("Quantity", 3, stockDto1.getQuantity().intValue());


    }
}
