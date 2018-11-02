package ro.msg.learning.shop.controllers;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.converters.CsvConverter;
import ro.msg.learning.shop.dtos.StockDto;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StockControllerIT {

    @LocalServerPort
    private int randomServerPort;
    private String resourcePath;
    private OAuth2RestTemplate oAuth2RestTemplate;
    private HttpHeaders headers = null;

    @Before
    public void setUp() {
        resourcePath = "http://localhost:" + randomServerPort;
        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setPassword("admin");
        resourceDetails.setUsername("admin");
        resourceDetails.setAccessTokenUri(resourcePath + "/oauth/token");
        resourceDetails.setClientId("my-trusted-client");
        resourceDetails.setScope(asList("read", "write", "trust"));
        resourceDetails.setClientSecret("secret");
        resourceDetails.setGrantType("password");

        DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();

        oAuth2RestTemplate = new OAuth2RestTemplate(resourceDetails, clientContext);
        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(new MediaType("text", "csv")));

    }

    @Test
    @SneakyThrows
    public void getTest() {

        ResponseEntity<String> response = oAuth2RestTemplate.getForEntity(
            resourcePath + "/api/stock/8",
            String.class);


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
    public void getFromCsvTest() {


        String s = "productId,locationId,quantity" + "\n" + "1,2,3" + "\n" + "1,2,3\n";

        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.valueOf("text/csv"));

        HttpEntity<String> httpEntity = new HttpEntity<>(s, headers);

        ResponseEntity<String> response = oAuth2RestTemplate.postForEntity(
            resourcePath + "/api/stock/fromcsv", httpEntity,
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
