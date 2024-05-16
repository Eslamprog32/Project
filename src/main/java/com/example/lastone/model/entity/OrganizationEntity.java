package com.example.lastone.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@Entity
@Table(name = "organizations")
@AllArgsConstructor
@NoArgsConstructor

public class OrganizationEntity {

    @Id
    @Column(name = "organization_name")
    private String organizationName;

    @Email
    private String email;

    private String location;
    private String phone;

    @JsonManagedReference
    @OneToOne(mappedBy = "organizationEntity")
    private PharmacistEntity pharmacistEntity;

    @JsonManagedReference
    @OneToOne(mappedBy = "organizationEntity")
    private LaboratoryEntity laboratoryEntity;

    @JsonManagedReference
    @OneToOne(mappedBy = "organizationEntity")
    private XRayLaboratoryEntity xRayLaboratoryEntity;

    @JsonManagedReference
    @OneToMany(mappedBy = "organizationEntity")
    private List<WorksInEntity> employees;
}
