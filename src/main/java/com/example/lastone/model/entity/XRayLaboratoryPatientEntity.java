package com.example.lastone.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "xrays_laboratory_patients")
@AllArgsConstructor
@NoArgsConstructor
//@Where(clause = "deleted_at is null")
public class XRayLaboratoryPatientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "XRays_laboratory_patients_id")
    private Long XRaysLaboratoryPatientsId;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "x_ray_laboratory_name")
    private String xRayLaboratoryName;

    private Boolean access;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "patient_name", insertable = false, updatable = false)
    private PatientEntity patientEntity;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "x_ray_laboratory_name", insertable = false, updatable = false)
    private XRayLaboratoryEntity xRayLaboratoryEntity;

}
