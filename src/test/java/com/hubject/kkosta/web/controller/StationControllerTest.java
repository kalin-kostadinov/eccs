package com.hubject.kkosta.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubject.kkosta.model.dto.StationDTO;
import com.hubject.kkosta.service.StationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(StationController.class)
class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StationService stationService;

    private StationDTO stationDTO;

    @BeforeEach
    void setUp() {
        stationDTO = new StationDTO();
        stationDTO.setId("001");
        stationDTO.setLatitude(52.5200);
        stationDTO.setLongitude(13.4050);
        stationDTO.setZipcode("10115");
    }

    @Test
    void createStation_WhenValidInput_ReturnsOk() throws Exception {
        when(stationService.createStation(any())).thenReturn(stationDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/stations/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(stationDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void createStation_ThrowsException_ReturnsBadRequest() throws Exception {
        when(stationService.createStation(any(StationDTO.class))).thenThrow(new RuntimeException("Test exception"));

        mockMvc.perform(MockMvcRequestBuilders.post("/stations/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(stationDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getStationById_WhenStationExists_ReturnsOk() throws Exception {
        when(stationService.getStationById(anyString())).thenReturn(stationDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/stations/001"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getStationById_WhenStationDoesNotExist_ReturnsNotFound() throws Exception {
        when(stationService.getStationById(anyString())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/stations/456"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void findStationsByZipcode_WhenStationsExist_ReturnsOk() throws Exception {
        when(stationService.findStationsByZipcode(anyString())).thenReturn(Collections.singletonList(stationDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/stations/zipcode/10115"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void findStationsByZipcode_WhenNoStationsExist_ReturnsNotFound() throws Exception {
        when(stationService.findStationsByZipcode(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/stations/zipcode/9090"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void findStationsWithinDistance_WhenStationsExistWithinDistance_ReturnsOk() throws Exception {
        when(stationService.findStationsWithinDistance(anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(Collections.singletonList(stationDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/stations/")
                        .param("latitude", "52.5200")
                        .param("longitude", "13.4050")
                        .param("maxDistance", "10.0"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void findStationsWithinDistance_WhenNoStationsExistWithinDistance_ReturnsNotFound() throws Exception {
        when(stationService.findStationsWithinDistance(anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/stations/")
                        .param("latitude", "52.5200")
                        .param("longitude", "13.4050")
                        .param("maxDistance", "10.0"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
