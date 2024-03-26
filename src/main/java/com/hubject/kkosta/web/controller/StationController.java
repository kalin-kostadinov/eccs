package com.hubject.kkosta.web.controller;

import com.hubject.kkosta.model.dto.StationDTO;
import com.hubject.kkosta.service.StationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    @Operation(summary = "Add a new charging station", description = "Returns the newly created charging station")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created", content =
                    { @Content(mediaType = "application/json", schema =
                      @Schema(implementation = StationDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid or missing data supplied")
    })
    @PostMapping("/")
    public ResponseEntity<?> createStation(@Valid @RequestBody StationDTO stationDTO) {
        try {
            StationDTO createdStation = stationService.createStation(stationDTO);
            return ResponseEntity.ok(createdStation);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Get station by ID", description = "Station must exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved station", content =
                    { @Content(mediaType = "application/json", schema =
                      @Schema(implementation = StationDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Station not found")})
    @GetMapping("/{id}")
    public ResponseEntity<?> getStationById(@PathVariable String id) {
        StationDTO station = stationService.getStationById(id);
        return station != null ?
                ResponseEntity.ok(station) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Station with ID " + id + " not found!");
    }

    @Operation(summary = "Get stations by Zipcode / Postal code", description =
            "Returns all stations in the provided Zipcode / Postal code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved stations", content =
                    { @Content(mediaType = "application/json", schema =
                      @Schema(implementation = StationDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Stations not found")})
    @GetMapping("/zipcode/{zipcode}")
    public ResponseEntity<?> findStationsByZipcode(@PathVariable String zipcode) {
        List<StationDTO> stations = stationService.findStationsByZipcode(zipcode);
        return !stations.isEmpty() ?
                ResponseEntity.ok(stations) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("No stations found for the provided zipcode!");
    }

    @Operation(summary = "Search stations by geolocation perimeter", description =
            "Retrieve stations within a specified distance around a given geolocation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved stations", content =
                    { @Content(mediaType = "application/json", array =
                      @ArraySchema(schema = @Schema(implementation = StationDTO.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid or missing data supplied"),
            @ApiResponse(responseCode = "404", description = "Stations not found")})
    @GetMapping("/")
    public ResponseEntity<?> findStationsWithinDistance(
            @RequestParam @Valid @DecimalMin("-90.0") @DecimalMax("90.0") Double latitude,
            @RequestParam @Valid @DecimalMin("-180.0") @DecimalMax("180.0") Double longitude,
            @RequestParam @Valid @Positive Double maxDistance) {
        List<StationDTO> stations = stationService.findStationsWithinDistance(latitude, longitude, maxDistance);
        return !stations.isEmpty() ?
                ResponseEntity.ok(stations) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("No stations found for the provided area!");
    }
}
