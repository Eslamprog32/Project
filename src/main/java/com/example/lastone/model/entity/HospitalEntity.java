package com.example.lastone.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "hospitals")
@AllArgsConstructor
@NoArgsConstructor
//@Where(clause = "deleted_at is null")
public class HospitalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_id")
    private Long hospitalId;
    @Column(name = "hospital_name")
    private String hospitalName;
    private String location;



}
