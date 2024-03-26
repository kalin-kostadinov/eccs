package com.hubject.kkosta.config;

import com.hubject.kkosta.model.dto.StationDTO;
import com.hubject.kkosta.model.entity.Station;
import org.flywaydb.core.Flyway;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(Station.class, StationDTO.class)
                .addMappings(mapper -> {
                    mapper.map(station -> station.getLocation().getLatitude(), StationDTO::setLatitude);
                    mapper.map(station -> station.getLocation().getLongitude(), StationDTO::setLongitude);
                    mapper.map(station -> station.getZipcode().getCode(), StationDTO::setZipcode);
                });
        return modelMapper;
    }

    @Bean
    FlywayMigrationInitializer flywayInitializer(Flyway flyway) {
        return new FlywayMigrationInitializer(flyway, (f) -> {
        });
    }

    @Bean
    @DependsOn("entityManagerFactory")
    Dummy delayedFlywayInitializer(Flyway flyway, FlywayProperties flywayProperties) {
        if (flywayProperties.isEnabled())
            flyway.migrate();
        return new Dummy();
    }

    static class Dummy {
    }
}
