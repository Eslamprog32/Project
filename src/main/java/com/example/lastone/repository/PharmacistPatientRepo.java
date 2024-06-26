package com.example.lastone.repository;

import com.example.lastone.model.entity.PharmacistPatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PharmacistPatientRepo extends JpaRepository<PharmacistPatientEntity,Long> {
    Optional<PharmacistPatientEntity> findByPharmacistNameAndPatientName(String pharmacistName, String patientName);
    List<PharmacistPatientEntity> findAllByPharmacistName(String pharmacistName);
    List<PharmacistPatientEntity> findAllByPatientName(String patientName);
}
