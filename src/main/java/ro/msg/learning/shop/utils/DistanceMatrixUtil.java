package ro.msg.learning.shop.utils;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.springframework.web.client.RestTemplate;
import ro.msg.learning.shop.dtos.DistanceMatrixDto;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.entities.embeddables.Address;

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


    //------------
    public DistanceMatrixDto getDistanceMatrixDto(List<Stock> stocks, Address address, String apiKey, RestTemplate restTemplate) {
        return restTemplate.getForObject(getMatrixUrl(stocks.stream().map(Stock::getLocation).collect(Collectors.toList()), address, apiKey), DistanceMatrixDto.class);
    }


    //------------
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


    public int[][] getDistancesMatrix(List<Stock> stocks, Address address, String apiKey, RestTemplate restTemplate) {
        int[][] distances = new int[stocks.size() + 1][stocks.size() + 1];
        List<Location> locations = stocks.stream().map(Stock::getLocation).collect(Collectors.toList());
        Location location = new Location();
        location.setAddress(address);
        locations.add(location);


        String origins = getOriginsForAllLocations(locations);
        String url = constructUrl(origins, origins, apiKey);
        val rows = restTemplate.getForObject(url, DistanceMatrixDto.class).getRows();


        for (int i = 0; i < rows.size() - 1; i++) {
            for (int j = 0; j < rows.size(); j++) {

                distances[i][j] = distances[j][i] = rows.get(i).getElements().get(j).getDistance().getValue();

            }

        }
        return distances;
    }
}
