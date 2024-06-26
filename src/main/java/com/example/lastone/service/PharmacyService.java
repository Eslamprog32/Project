package com.example.lastone.service;

import com.example.lastone.model.dto.ConnectionsListDTO;
import com.example.lastone.model.dto.MedicineDTO;
import com.example.lastone.model.dto.PrescriptionDTOToViewAsList;

import java.util.List;

public interface PharmacyService {
    int canAccessThisLab(String pharmacistName);

    String getAccess(String pharmacistName, String patientName) throws Exception;

    String acceptAccess(String pharmacistName, String patientName) throws Exception;

    String removeAccess(String pharmacistName, String patientName) throws Exception;

    List<ConnectionsListDTO> getListOfConnections(String pharmacistName);

    List<PrescriptionDTOToViewAsList> getAllPrescriptionTOMyPatient(String pharmacistName
            , String patientName) throws Exception;

    List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByID(String xRayLaboratoryName, String patientName) throws Exception;

    List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByDoctorName(String xRayLaboratoryName, String patientName) throws Exception;

    List<MedicineDTO> getPrescription(Long id, String pharmacistName);

}
