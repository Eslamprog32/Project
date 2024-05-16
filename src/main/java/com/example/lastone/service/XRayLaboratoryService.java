package com.example.lastone.service;

import com.example.lastone.model.dto.PrescriptionViewDTO;

import java.util.List;

public interface XRayLaboratoryService {
    Boolean acceptAccess(String xRayLaboratoryName, String patientName) throws Exception;

    Boolean removeAccess(String xRayLaboratoryName, String patientName) throws Exception;

    Boolean getAccess(String xRayLaboratoryName, String patientName) throws Exception;


    List<PrescriptionViewDTO> getAllPrescriptionTOMyPatient(String xRayLaboratoryName, String patientName) throws Exception;
}
