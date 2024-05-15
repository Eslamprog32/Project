package com.example.lastone.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "works_in")
@AllArgsConstructor
@NoArgsConstructor
public class WorksInEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_id")
    private Integer workId;
    @Column(name = "user_name")
    private String username;
    @Column(name = "organization_name")
    private String organizationName;

    @CreationTimestamp
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_name",insertable = false,updatable = false)
    private UserEntity userEntity;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "organization_name", insertable = false, updatable = false)
    private OrganizationEntity organizationEntity;


}