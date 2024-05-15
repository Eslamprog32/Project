package com.example.lastone.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "allergies")
@AllArgsConstructor
@NoArgsConstructor
//@Where(clause = "deleted_at is null")
public class AllergieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allergie_id")
    private Long allergieId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    private String description;
    private String type;
    private String category;
    private String reaction;
    private String severity;

}
