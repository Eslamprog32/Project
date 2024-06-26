package com.example.lastone.repository;

import com.example.lastone.model.entity.XRayLaboratoryPatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface XRayLaboratoryPatientRepo extends JpaRepository<XRayLaboratoryPatientEntity, Long> {

    Optional<XRayLaboratoryPatientEntity> findByLaboratoryNameAndPatientName(String patientName
            , String xRayLaboratoryName);

    List<XRayLaboratoryPatientEntity> findAllByLaboratoryName(String LabName);

    List<XRayLaboratoryPatientEntity> findAllByPatientName(String patientName);
}
