package com.example.lastone.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "xrays")
@AllArgsConstructor
@NoArgsConstructor
public class XRayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "xRay_id")
    private Long xRayId;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "xRay_name")
    private String xRayName;
    @Column(name = "picture")
    private String picture;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "patient_name", insertable = false, updatable = false)
    private PatientEntity patientEntity;
}
