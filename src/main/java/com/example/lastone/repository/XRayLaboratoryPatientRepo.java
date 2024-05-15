package com.example.lastone.repository;

import com.example.lastone.model.entity.PatientEntity;
import com.example.lastone.model.entity.XRayLaboratoryEntity;
import com.example.lastone.model.entity.XRayLaboratoryPatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface XRayLaboratoryPatientRepo extends JpaRepository<XRayLaboratoryPatientEntity, Long> {
    /*
    Optional<XRayLaboratoryPatientEntity> findByPatientNameAndAndXRayLaboratoryName(String patientName
            , String xRayLaboratoryName);

     */
}
