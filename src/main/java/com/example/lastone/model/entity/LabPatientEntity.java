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
@Table(name = "labs_patients")
@AllArgsConstructor
@NoArgsConstructor
//@Where(clause = "deleted_at is null")
public class LabPatientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lab_patient_id")
    private Long labPatientId;
    @Column(name = "patient_name")
    private String patientName;
    @Column(name = "laboratory_name")
    private String laboratoryName;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "patient_name", insertable = false, updatable = false)
    private PatientEntity patientEntity;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "laboratory_name", insertable = false, updatable = false)
    private LaboratoryEntity laboratoryEntity;

}
