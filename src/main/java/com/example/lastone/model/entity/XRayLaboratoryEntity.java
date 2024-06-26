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
@Table(name = "xray_laboratories")
@AllArgsConstructor
@NoArgsConstructor
public class XRayLaboratoryEntity {

    @Id
    @Column(name = "x_ray_laboratory_name")
    private String xRayLaboratoryName;

    @JsonManagedReference
    @OneToMany(mappedBy = "xRayLaboratoryEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<XRayLaboratoryPatientEntity> xRayLaboratoryPatientEntities;

    @JsonBackReference
    @OneToOne( fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "x_ray_laboratory_name", insertable = false, updatable = false)
    private OrganizationEntity organizationEntity;
}
