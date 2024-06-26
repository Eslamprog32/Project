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
@Table(name = "tests_lab")
@AllArgsConstructor
@NoArgsConstructor
public class TestsLabEntity {
    @Id
    @Column(name = "tests_lab_name")
    private String testsLaboratoryName;

    @JsonManagedReference
    @OneToMany(mappedBy = "testsLabEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TestsLabPatientEntity> testsLabPatientEntities;

    @JsonBackReference
    @OneToOne( fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "tests_lab_name", insertable = false, updatable = false)
    private OrganizationEntity organizationEntity;
}
