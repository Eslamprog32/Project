package com.example.lastone.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@Entity
@Table(name = "pharmacists")
@AllArgsConstructor
@NoArgsConstructor
//@Where(clause = "deleted_at is null")
public class PharmacistEntity {

    @Id
    @Column(name = "pharmacist_name")
    private String pharmacistName;


    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "pharmacist_name", insertable = false, updatable = false)
    private OrganizationEntity organizationEntity;

    @JsonManagedReference
    @OneToMany(mappedBy = "pharmacistEntity")
    private List<PharmaceuticalEntity> pharmaceuticalEntities;

    @JsonManagedReference
    @OneToMany(mappedBy = "pharmacistEntity")
    private List<PharmacistPatientEntity> pharmacistPatientEntities;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "pharmacist_name", insertable = false, updatable = false)
    private UserEntity userEntity;

}
