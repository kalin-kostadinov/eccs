package com.hubject.kkosta.repository;

import com.hubject.kkosta.model.entity.Geolocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GeolocationRepository extends JpaRepository<Geolocation, Long> {

    Optional<Geolocation> findByLatitudeAndLongitude(double latitude, double longitude);
}
