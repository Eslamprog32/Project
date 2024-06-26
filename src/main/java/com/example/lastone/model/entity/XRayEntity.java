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

    @Lob
    @Column(name = "picture", length = 64 * 1024)
    private byte[] picture;

    private String category;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonBackReference
    @ManyToOne( fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_name", insertable = false, updatable = false)
    private PatientEntity patientEntity;
}
