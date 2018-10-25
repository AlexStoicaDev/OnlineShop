package ro.msg.learning.shop.distance_APIs;

import ro.msg.learning.shop.entities.Location;

import java.util.List;

public interface DistanceAPI {
    int[][] getDistancesMatrix(List<Location> locations);
}

