package com.example.lastone.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "test")
@AllArgsConstructor
@NoArgsConstructor
public class Test {
    @Id
    private Long id;
    @Column(name = "medicine_and_note")
    private String medicineAndNote;
}
