package com.example.lastone.service;

import com.example.lastone.model.dto.ConnectionsListDTO;
import com.example.lastone.model.dto.PrescriptionDTOToViewAsList;
import com.example.lastone.model.dto.XRayInPrescriptionDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface XRayLaboratoryService {
    String acceptAccess(String xRayLaboratoryName, String patientName) throws Exception;

    String removeAccess(String xRayLaboratoryName, String patientName) throws Exception;

    List<ConnectionsListDTO> getListOfConnections(String xRayLaboratoryName);

    String getAccess(String xRayLaboratoryName, String patientName) throws Exception;


    List<PrescriptionDTOToViewAsList> getAllPrescriptionTOMyPatient(String xRayLaboratoryName, String patientName) throws Exception;

    int canAccessThisLab(String xRayLaboratoryName);

    List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByID(String xRayLaboratoryName, String patientName) throws Exception;

    List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByDoctorName(String xRayLaboratoryName
            ,String patientName) throws Exception;


    List<XRayInPrescriptionDTO> getPrescription(Long id, String xRayLaboratoryName);

    String addXRayResult(String xRayLaboratoryName, String patientName
            , String category, MultipartFile file) throws IOException;
}
