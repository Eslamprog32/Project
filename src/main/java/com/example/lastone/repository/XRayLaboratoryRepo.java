package com.example.lastone.repository;

import com.example.lastone.model.entity.PatientEntity;
import com.example.lastone.model.entity.XRayLaboratoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface XRayLaboratoryRepo extends JpaRepository<XRayLaboratoryEntity,String> {
}
