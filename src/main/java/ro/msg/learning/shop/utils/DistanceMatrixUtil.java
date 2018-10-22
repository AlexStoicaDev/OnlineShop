package ro.msg.learning.shop.utils;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.springframework.web.client.RestTemplate;
import ro.msg.learning.shop.dtos.DistanceMatrixDto;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.embeddables.Address;

import java.util.List;

/**
 * responsible for controlling the interactions with the GOOGLE DistanceMatrixApi
 */
@UtilityClass
public class DistanceMatrixUtil {


    /**
     * @param origins      string that contains the locations in the format required by the api
     * @param destinations string that contains the locations in the format required by the api
     * @return the url used for calling the distance matrix api
     */
    private String constructUrl(String origins, String destinations, String apiKey) {
        return "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial"
            + "&origins=" + origins
            + "&destinations=" + destinations
            + "&key=" + apiKey;
    }


    /**
     * @param address destination address for the order
     * @return address as string in the format required by the api
     */
    private String getAddressAsStringForDistanceMatrix(Address address) {

        return getCityAsStringForDistanceMatrix(address.getCity()) + getCountryAsStringForDistanceMatrix(address.getCountry());
    }

    /**
     * @param city name of the city
     * @return name of the city  as string in the format required by the api
     */

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

    /**
     * @param country name of the city
     * @return name of the country as string in the format required by the api
     */
    private String getCountryAsStringForDistanceMatrix(String country) {

        final val split1 = country.split(" ");

        StringBuilder stringBuilder = new StringBuilder();
        for (String s : split1) {
            stringBuilder.append(s).append("+");
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("+"));
        return stringBuilder.toString();
    }

    /**
     * @param locations list of locations
     * @return a string that contains all locations in the format  required by the api
     */
    private String getOriginsForAllLocations(List<Location> locations) {

        StringBuilder stringBuilder = new StringBuilder();
        for (Location l : locations) {
            stringBuilder.append(getAddressAsStringForDistanceMatrix(l.getAddress())).append('|');
        }
        return stringBuilder.toString().substring(0, stringBuilder.lastIndexOf("|"));

    }

    /**
     * @param locations list of locations
     * @return a matrix that contains all the distances between every location from the locations list
     */
    public int[][] getDistancesMatrix(List<Location> locations, String apiKey, RestTemplate restTemplate) {
        int[][] distances = new int[locations.size()][locations.size()];
        String origins = getOriginsForAllLocations(locations);
        String url = constructUrl(origins, origins, apiKey);
        val rows = restTemplate.getForObject(url, DistanceMatrixDto.class).getRows();


        for (int i = 0; i < rows.size(); i++) {
            for (int j = 0; j < rows.size(); j++) {
                if (i == j) {
                    distances[i][j] = Integer.MAX_VALUE;
                } else distances[i][j] = distances[j][i] = rows.get(i).getElements().get(j).getDistance().getValue();

            }

        }
        return distances;
    }
}
