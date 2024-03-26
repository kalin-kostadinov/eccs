package com.hubject.kkosta.util;

import com.hubject.kkosta.model.entity.Geolocation;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for geographical calculations.
 */
public class GeoUtil {

    // Radius of the Earth in kilometers
    private static final double EARTH_RADIUS = 6371;

    /**
     * Calculates the distance between two geographical points using the Haversine formula.
     *
     * @param lat1 Latitude of the first point.
     * @param lon1 Longitude of the first point.
     * @param lat2 Latitude of the second point.
     * @param lon2 Longitude of the second point.
     * @return The distance between the two points in kilometers.
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    /**
     * Finds geolocations within a specified distance from a center point.
     *
     * @param centerLat   Latitude of the center point.
     * @param centerLon   Longitude of the center point.
     * @param maxDistance The maximum distance in kilometers.
     * @param locations   List of geolocations to search within.
     * @return A list of geolocations within the specified distance from the center point.
     */
    public static List<Geolocation> findWithinDistance(double centerLat, double centerLon,
                                                       double maxDistance, List<Geolocation> locations) {
        return locations.stream()
                .filter(location ->
                        calculateDistance(centerLat, centerLon, location.getLatitude(), location.getLongitude())
                                <= maxDistance)
                .collect(Collectors.toList());
    }
}
