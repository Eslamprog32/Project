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
@Table(name = "tests_in_prescription")
@AllArgsConstructor
@NoArgsConstructor
public class TestsEntity {
    @Id
    @Column(name = "patient_name")
    private String patientName;
    private String test;
    private String note;

}
