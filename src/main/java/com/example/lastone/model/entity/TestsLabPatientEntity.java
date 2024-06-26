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
@Table(name = "tests_laboratory_patients")
@AllArgsConstructor
@NoArgsConstructor
public class TestsLabPatientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tests_laboratory_patients_id")
    private Long testsLaboratoryPatientsId;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "tests_lab_name")
    private String laboratoryName;

    private Boolean access;

    private Boolean whichRequestAccess;

    @JsonBackReference
    @ManyToOne( fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_name", insertable = false, updatable = false)
    private PatientEntity patientEntity;

    @JsonBackReference
    @ManyToOne( fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "tests_lab_name", insertable = false, updatable = false)
    private TestsLabEntity testsLabEntity;
}
