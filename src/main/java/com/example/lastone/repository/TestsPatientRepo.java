package com.example.lastone.repository;

import com.example.lastone.model.entity.TestsLabPatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TestsPatientRepo extends JpaRepository<TestsLabPatientEntity, Long> {
    Optional<TestsLabPatientEntity> findByLaboratoryNameAndPatientName(String laboratoryName, String patientName);

    List<TestsLabPatientEntity> findAllByLaboratoryName(String laboratoryName);

    List<TestsLabPatientEntity> findAllByPatientName(String patientName);
}
