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
@Table(name = "pharmaceuticals")
@AllArgsConstructor
@NoArgsConstructor
//@Where(clause = "deleted_at is null")
public class PharmaceuticalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pharmaceuticals_id")
    private Integer pharmaceuticalsId;
    @Column(name = "pharmaceuticals_name")
    private String pharmaceuticalsName;
    @Column(name = "pharmaceuticals_count")
    private Integer pharmaceuticalsCount;
    @Column(name = "pharmacist_name")
    private String pharmacistName;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "pharmacist_name", insertable = false, updatable = false)
    private PharmacistEntity pharmacistEntity;
}
