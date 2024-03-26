package com.hubject.kkosta.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StationDTO {

    @Schema(name = "id", description = "Station ID", example = "001")
    @NotNull
    private String id;
    @Schema(name = "latitude", description = "Station latitude", example = "52.5200")
    @NotNull
    private Double latitude;
    @Schema(name = "longitude", description = "Station longitude", example = "13.4050")
    @NotNull
    private Double longitude;
    @Schema(name = "zipcode", description = "Station zipcode", example = "10115")
    @NotNull
    private String zipcode;

}
