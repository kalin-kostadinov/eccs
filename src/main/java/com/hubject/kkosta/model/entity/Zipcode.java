package com.hubject.kkosta.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "zipcodes")
public class Zipcode extends BaseEntity{

    @Column(unique = true, nullable = false)
    private String code;
}
