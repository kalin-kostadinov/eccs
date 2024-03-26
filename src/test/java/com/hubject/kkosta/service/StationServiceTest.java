package com.hubject.kkosta.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hubject.kkosta.model.dto.StationDTO;
import com.hubject.kkosta.model.entity.Geolocation;
import com.hubject.kkosta.model.entity.Station;
import com.hubject.kkosta.model.entity.Zipcode;
import com.hubject.kkosta.repository.GeolocationRepository;
import com.hubject.kkosta.repository.StationRepository;
import com.hubject.kkosta.repository.ZipcodeRepository;
import org.modelmapper.ModelMapper;

class StationServiceTest {

    @Mock
    private GeolocationRepository geolocationRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private ZipcodeRepository zipcodeRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private StationService stationService;

    private Zipcode zipcode;
    private Geolocation geolocation;
    private Station station;
    private StationDTO stationDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        stationDTO = new StationDTO();
        stationDTO.setId("001");
        stationDTO.setLatitude(12.3456);
        stationDTO.setLongitude(78.9999);
        stationDTO.setZipcode("10115");

        geolocation = new Geolocation(12.3456, 78.9999);
        zipcode = new Zipcode("10115");

        station = new Station();
        station.setId("001");
        station.setZipcode(zipcode);
        station.setLocation(geolocation);
    }

    @Test
    void createStation_WhenStationDoesNotExist_ShouldCreateNewStation() {
        when(stationRepository.existsById(stationDTO.getId())).thenReturn(false);
        when(geolocationRepository.findByLatitudeAndLongitude(stationDTO.getLatitude(), stationDTO.getLongitude()))
                .thenReturn(Optional.empty());
        when(zipcodeRepository.findByCode(stationDTO.getZipcode())).thenReturn(Optional.empty());
        when(geolocationRepository.save(any())).thenReturn(geolocation);
        when(zipcodeRepository.save(any())).thenReturn(zipcode);
        when(stationRepository.save(any())).thenReturn(station);
        when(modelMapper.map(any(), any())).thenReturn(stationDTO);

        StationDTO createdStationDTO = stationService.createStation(stationDTO);

        assertEquals(stationDTO.getId(), createdStationDTO.getId());
    }

    @Test
    void getStationById_WhenStationExists_ShouldReturnStationDTO() {
        when(stationRepository.findById(stationDTO.getId())).thenReturn(Optional.of(station));
        when(modelMapper.map(any(), any())).thenReturn(stationDTO);

        StationDTO retrievedStationDTO = stationService.getStationById(stationDTO.getId());

        assertEquals(stationDTO.getId(), retrievedStationDTO.getId());
    }

    @Test
    void getStationById_WhenStationDoesNotExist_ShouldReturnNull() {
        when(stationRepository.findById(any())).thenReturn(Optional.empty());

        StationDTO retrievedStationDTO = stationService.getStationById("nonexistent_id");

        assertNull(retrievedStationDTO);
    }

    @Test
    void findStationsByZipcode_WhenStationsExist_ShouldReturnListOfStationDTOs() {
        when(stationRepository.findByZipcodeCode(stationDTO.getZipcode())).thenReturn(Collections.singletonList(station));

        List<StationDTO> retrievedStations = stationService.findStationsByZipcode(stationDTO.getZipcode());

        assertEquals(1, retrievedStations.size());
    }

    @Test
    void findStationsByZipcode_WhenNoStationsExist_ShouldReturnEmptyList() {
        when(stationRepository.findByZipcodeCode(any())).thenReturn(Collections.emptyList());

        List<StationDTO> retrievedStations = stationService.findStationsByZipcode("nonexistent_zipcode");

        assertEquals(0, retrievedStations.size());
    }

    @Test
    void findStationsWithinDistance_WhenStationsExistWithinDistance_ShouldReturnListOfStationDTOs() {
        when(geolocationRepository.findAll()).thenReturn(Collections.singletonList(geolocation));
        when(stationRepository.findAllByGeolocationIds(any())).thenReturn(Collections.singletonList(station));

        List<StationDTO> retrievedStations = stationService.findStationsWithinDistance(geolocation.getLatitude(),
                geolocation.getLongitude(), 100.00);

        assertEquals(1, retrievedStations.size());
    }

    @Test
    void findStationsWithinDistance_WhenNoStationsExistWithinDistance_ShouldReturnEmptyList() {
        when(geolocationRepository.findAll()).thenReturn(Collections.singletonList(geolocation));

        List<Long> geolocationIds = Collections.emptyList();
        when(stationRepository.findAllByGeolocationIds(geolocationIds)).thenReturn(Collections.singletonList(station));

        List<StationDTO> retrievedStations = stationService.findStationsWithinDistance(geolocation.getLatitude(),
                geolocation.getLongitude(), 100.00);

        assertEquals(0, retrievedStations.size());
    }

    @Test
    void validateStationId_WhenStationExists_ShouldThrowRuntimeException() {
        String existingStationId = "123";
        when(stationRepository.existsById(existingStationId)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> stationService.validateStationId(existingStationId));
    }

    @Test
    void validateStationId_WhenStationDoesNotExist_ShouldNotThrowException() {
        String nonExistingStationId = "456";
        when(stationRepository.existsById(nonExistingStationId)).thenReturn(false);

        assertDoesNotThrow(() -> stationService.validateStationId(nonExistingStationId));
    }
}
