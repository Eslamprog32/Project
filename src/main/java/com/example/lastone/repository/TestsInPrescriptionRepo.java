package com.example.lastone.repository;

import com.example.lastone.model.entity.TestsInPrescriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TestsInPrescriptionRepo extends JpaRepository<TestsInPrescriptionEntity, Long> {
    List<TestsInPrescriptionEntity> findAllByPrescriptionId(Long id);

}
