package com.hubject.kkosta.service;

import com.hubject.kkosta.model.dto.StationDTO;
import com.hubject.kkosta.model.entity.Geolocation;
import com.hubject.kkosta.model.entity.Station;
import com.hubject.kkosta.model.entity.Zipcode;
import com.hubject.kkosta.repository.GeolocationRepository;
import com.hubject.kkosta.repository.StationRepository;
import com.hubject.kkosta.repository.ZipcodeRepository;
import com.hubject.kkosta.util.GeoUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StationService {

    private final GeolocationRepository geolocationRepository;
    private final StationRepository stationRepository;
    private final ZipcodeRepository zipcodeRepository;
    private final ModelMapper modelMapper;

    /**
     * Creates a new station.
     *
     * @param stationDTO The DTO representing the station to be created.
     * @return The DTO representing the created station.
     */
    @Transactional
    public StationDTO createStation(StationDTO stationDTO) {
        validateStationId(stationDTO.getId());
        Geolocation geolocation = getOrCreateGeolocation(stationDTO.getLatitude(), stationDTO.getLongitude());
        Zipcode zipcode = getOrCreateZipcode(stationDTO.getZipcode());
        Station station = new Station(stationDTO.getId(), geolocation, zipcode);
        return modelMapper.map(station, StationDTO.class);
    }

    /**
     * Retrieves a station by its ID.
     *
     * @param id The ID of the station to retrieve.
     * @return The DTO representing the retrieved station, or null if not found.
     */
    public StationDTO getStationById(String id) {
        return stationRepository.findById(id)
                .map(s -> modelMapper.map(s, StationDTO.class))
                .orElse(null);
    }

    /**
     * Retrieves stations by zipcode.
     *
     * @param zipcode The zipcode to search for.
     * @return A list of DTOs representing the retrieved stations.
     */
    public List<StationDTO> findStationsByZipcode(String zipcode) {
        return stationRepository.findByZipcodeCode(zipcode)
                .stream()
                .map(s -> modelMapper.map(s, StationDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves stations within a specified distance from a given geolocation.
     *
     * @param latitude    The latitude of the center geolocation.
     * @param longitude   The longitude of the center geolocation.
     * @param maxDistance The maximum distance in kilometers.
     * @return A list of DTOs representing the retrieved stations.
     */
    public List<StationDTO> findStationsWithinDistance(Double latitude, Double longitude, Double maxDistance) {
        List<Geolocation> withinDistance = GeoUtil.findWithinDistance(latitude, longitude, maxDistance, geolocationRepository.findAll());
        List<Long> geolocationIds = withinDistance.stream().map(Geolocation::getId).collect(Collectors.toList());
        return stationRepository.findAllByGeolocationIds(geolocationIds)
                .stream()
                .map(s -> modelMapper.map(s, StationDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Validates if a station with the given ID already exists.
     * Throws a RuntimeException if the station ID is found in the repository.
     *
     * @param id The ID of the station to validate.
     */
    void validateStationId(String id) {
        if (stationRepository.existsById(id)) {
            throw new RuntimeException("Station with ID " + id + " already exists!");
        }
    }

    /**
     * Retrieves an existing geolocation with the given latitude and longitude,
     * or creates a new geolocation if none exists.
     *
     * @param latitude  The latitude of the geolocation.
     * @param longitude The longitude of the geolocation.
     * @return The existing or newly created geolocation.
     */
    private Geolocation getOrCreateGeolocation(Double latitude, Double longitude) {
        return geolocationRepository.findByLatitudeAndLongitude(latitude, longitude)
                .orElseGet(() -> geolocationRepository.save(new Geolocation(latitude, longitude)));
    }

    /**
     * Retrieves an existing zipcode with the given code,
     * or creates a new zipcode if none exists.
     *
     * @param code The code of the zipcode.
     * @return The existing or newly created zipcode.
     */
    private Zipcode getOrCreateZipcode(String code) {
        return zipcodeRepository.findByCode(code)
                .orElseGet(() -> zipcodeRepository.save(new Zipcode(code)));
    }
}