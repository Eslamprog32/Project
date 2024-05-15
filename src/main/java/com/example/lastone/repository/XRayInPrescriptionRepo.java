package com.example.lastone.repository;

import com.example.lastone.model.entity.XRayEntity;
import com.example.lastone.model.entity.XRayInPrescriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface XRayInPrescriptionRepo extends JpaRepository<XRayInPrescriptionEntity, Long> {
    List<XRayInPrescriptionEntity> findAllByPrescriptionId(Long id);
}
