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
@Table(name = "laboratories")
@AllArgsConstructor
@NoArgsConstructor
public class LaboratoryEntity {

    @Id
    @Column(name = "laboratory_name")
    private String laboratoryName;

    @JsonManagedReference
    @OneToMany(mappedBy = "laboratoryEntity")
    private List<LabPatientEntity> labPatientEntities;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "laboratory_name", insertable = false, updatable = false)
    private OrganizationEntity organizationEntity;
}
