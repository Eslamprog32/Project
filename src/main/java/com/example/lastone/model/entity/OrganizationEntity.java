package com.example.lastone.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@Entity
@Table(name = "organizations")
@AllArgsConstructor
@NoArgsConstructor

public class OrganizationEntity {

    @Id
    @Column(name = "organization_name")
    private String organizationName;
    @Email
    private String email;
    private String location;
    private String phone;
    private String type;

    @JsonManagedReference
    @OneToOne(mappedBy = "organizationEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PharmacistEntity pharmacistEntity;
    @Lob
    @Column(name = "picture", length = 64 * 1024)
    private byte[] picture;
    @JsonManagedReference
    @OneToOne(mappedBy = "organizationEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private TestsLabEntity testsLabEntity;

    @JsonManagedReference
    @OneToOne(mappedBy = "organizationEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private XRayLaboratoryEntity xRayLaboratoryEntity;

    @JsonManagedReference
    @OneToMany(mappedBy = "organizationEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<WorksInEntity> employees;
}
