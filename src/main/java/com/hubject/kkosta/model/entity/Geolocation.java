package com.hubject.kkosta.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"latitude", "longitude"})}, name = "geolocations")
@Data
@EqualsAndHashCode(callSuper = true, of = {"latitude", "longitude"})
@AllArgsConstructor
@NoArgsConstructor
public class Geolocation extends BaseEntity{

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;
}
