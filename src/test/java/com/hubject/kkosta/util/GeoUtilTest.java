package com.hubject.kkosta.util;

import com.hubject.kkosta.model.entity.Geolocation;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeoUtilTest {

    @Test
    void calculateDistance_ShouldCalculateCorrectDistance() {
        // Coordinates of Berlin (52.5200° N, 13.4050° E)
        double lat1 = 52.5200;
        double lon1 = 13.4050;

        // Coordinates of Munich (48.1351° N, 11.5820° E)
        double lat2 = 48.1351;
        double lon2 = 11.5820;

        // Expected distance between Berlin and Munich (in km)
        double expectedDistance = 504.4;

        // Calculate the distance
        double actualDistance = GeoUtil.calculateDistance(lat1, lon1, lat2, lon2);

        // Assert that the calculated distance is within a small margin of error
        assertEquals(expectedDistance, actualDistance, 0.2);
    }

    @Test
    void findWithinDistance_ShouldReturnGeolocationsWithinDistance() {

        double centerLat = 52.5200;
        double centerLon = 13.4050;
        double maxDistance = 100;

        List<Geolocation> locations = new ArrayList<>();
        // Geolocations within the specified distance
        locations.add(new Geolocation(52.5244, 13.4105));
        locations.add(new Geolocation(52.5166, 13.3816));
        // Geolocations outside the specified distance
        locations.add(new Geolocation(53.5186, 14.4015));
        locations.add(new Geolocation(48.1351, 11.5820));

        List<Geolocation> withinDistance = GeoUtil.findWithinDistance(centerLat, centerLon, maxDistance, locations);

        assertEquals(2, withinDistance.size());
    }
}
