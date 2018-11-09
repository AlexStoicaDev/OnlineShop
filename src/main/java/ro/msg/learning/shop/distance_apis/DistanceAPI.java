package ro.msg.learning.shop.distance_apis;

import ro.msg.learning.shop.entities.Location;

import java.util.List;

public interface DistanceAPI {
    int[][] getDistancesMatrix(List<Location> locations);
}

