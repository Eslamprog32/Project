package com.example.lastone.repository;

import com.example.lastone.model.entity.TestsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestsRepo extends JpaRepository<TestsEntity, Long> {
    List<TestsEntity> findAllByPatientName(String patientName);
}
