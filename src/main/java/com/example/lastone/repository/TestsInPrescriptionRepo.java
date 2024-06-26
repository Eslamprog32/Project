package com.example.lastone.repository;

import com.example.lastone.model.entity.TestsPrescriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TestsInPrescriptionRepo extends JpaRepository<TestsPrescriptionEntity, Long> {
    List<TestsPrescriptionEntity> findAllByPrescriptionId(Long id);

}
