
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
@Table(name = "pharmacist_patient")
@AllArgsConstructor
@NoArgsConstructor
public class PharmacistPatientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pharmacist_patient_id")
    private Long pharmacistPatientID;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "pharmacist_name")
    private String pharmacistName;


    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "patient_name", insertable = false, updatable = false)
    private PatientEntity patientEntity;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "pharmacist_name", insertable = false, updatable = false)
    private PharmacistEntity pharmacistEntity;

}
