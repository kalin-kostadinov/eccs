package com.hubject.kkosta.repository;

import com.hubject.kkosta.model.entity.Zipcode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ZipcodeRepository extends JpaRepository<Zipcode, Long> {

    Optional<Zipcode> findByCode(String code);
}
