package com.example.lastone.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@Entity
@Table(name = "tests_in_prescription")
@AllArgsConstructor
@NoArgsConstructor
public class TestsInPrescriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tests_id")
    private Long id;

    @Column(name = "prescription_id")
    private Long prescriptionId;

    private String test;
    private String note;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "prescription_id", insertable = false, updatable = false)
    PrescriptionEntity prescriptionEntity;
}
