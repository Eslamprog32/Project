package com.example.lastone.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "immunizations")
@AllArgsConstructor
@NoArgsConstructor
//@Where(clause = "deleted_at is null")
public class ImmunizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "immunization_id")
    private Long immunizationId;
    @Column(name = "user_name")
    private String userName;
    private String data;
    private String description;


}
