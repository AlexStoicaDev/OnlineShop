package ro.msg.learning.shop.utils;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.springframework.web.client.RestTemplate;
import ro.msg.learning.shop.dtos.DistanceMatrixDto;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.entities.embeddables.Address;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@UtilityClass
public class DistanceMatrixUtil {


    private String constructUrl(String origins, String destinations, String apiKey) {
        return "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial"
            + "&origins=" + origins
            + "&destinations=" + destinations
            + "&key=" + apiKey;
    }

    private String getMatrixUrl(List<Location> locations, Address address, String apiKey) {
        String origins = getOriginsForAllLocations(locations);
        String destinations = getAddressAsStringForDistanceMatrix(address);
        return constructUrl(origins, destinations, apiKey);
    }

    private String getMatrixUrl(Stock stock1, Stock stock2, String apiKey) {
        String origins = getOriginsForAllLocations(Collections.singletonList(stock1.getLocation()));
        String destinations = getOriginsForAllLocations(Collections.singletonList(stock2.getLocation()));
        return constructUrl(origins, destinations, apiKey);
    }

    public Map<Stock, Integer> getStockDistanceMap(List<Stock> stocks, Address address, String apiKey, RestTemplate restTemplate) {

        Map<Stock, Integer> stockDistanceFromDestinationMap = new HashMap<>();

        List<DistanceMatrixDto.Distance> distances = restTemplate.getForObject(getMatrixUrl(stocks.stream().map(Stock::getLocation).collect(Collectors.toList()), address, apiKey), DistanceMatrixDto.class).getRows().parallelStream()
            .map(DistanceMatrixDto.Row::getElements)
            .flatMap(List::stream)
            .map(DistanceMatrixDto.Element::getDistance)
            .collect(Collectors.toList());

        for (DistanceMatrixDto.Distance distance : distances) {
            if (distance != null) {
                stockDistanceFromDestinationMap.put(stocks.get(0), distance.getValue());
            }
            stocks.remove(0);
        }
        return stockDistanceFromDestinationMap;
    }

    public Integer getDistanceBetweenTwoStocks(Stock stock1, Stock stock2, String apiKey, RestTemplate restTemplate) {
        try {
            return restTemplate.getForObject(getMatrixUrl(stock1, stock2, apiKey), DistanceMatrixDto.class).getRows().get(0).getElements().get(0).getDistance().getValue();
        } catch (NullPointerException ex) {
            return 0;
        }
    }

    private String getAddressAsStringForDistanceMatrix(Address address) {

        return getCityAsStringForDistanceMatrix(address.getCity()) + getCountryAsStringForDistanceMatrix(address.getCountry());
    }

    private String getCityAsStringForDistanceMatrix(String city) {

        final val split = city.split(" ");

        StringBuilder stringBuilder = new StringBuilder();
        for (String s : split) {
            stringBuilder.append(s).append("+");
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("+"));
        stringBuilder.append(",");
        return stringBuilder.toString();
    }

    private String getCountryAsStringForDistanceMatrix(String country) {

        final val split1 = country.split(" ");

        StringBuilder stringBuilder = new StringBuilder();
        for (String s : split1) {
            stringBuilder.append(s).append("+");
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("+"));
        return stringBuilder.toString();
    }

    private String getOriginsForAllLocations(List<Location> locations) {

        StringBuilder stringBuilder = new StringBuilder();
        for (Location l : locations) {
            stringBuilder.append(getAddressAsStringForDistanceMatrix(l.getAddress())).append('|');
        }
        return stringBuilder.toString().substring(0, stringBuilder.lastIndexOf("|"));

    }
}
