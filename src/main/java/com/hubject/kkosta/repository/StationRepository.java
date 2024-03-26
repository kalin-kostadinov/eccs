package com.hubject.kkosta.repository;

import com.hubject.kkosta.model.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, String> {

    Optional<Station> findStationById(String id);

    List<Station> findByZipcodeCode(String code);

    @Query("SELECT s FROM Station s WHERE s.location.id IN :geolocationIds")
    List<Station> findAllByGeolocationIds(List<Long> geolocationIds);
}
