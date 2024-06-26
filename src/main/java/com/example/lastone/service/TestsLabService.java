package com.example.lastone.service;

import com.example.lastone.model.dto.ConnectionsListDTO;
import com.example.lastone.model.dto.PrescriptionDTOToViewAsList;
import com.example.lastone.model.dto.TestsInPrescriptionDTO;
import com.example.lastone.model.dto.TestsResultDTO;

import java.util.List;

public interface TestsLabService {
    int canAccessThisLab(String organizationName);

    String getAccess(String laboratoryName, String patientName) throws Exception;

    String acceptAccess(String laboratoryName, String patientName) throws Exception;

    String removeAccess(String laboratoryName, String patientName) throws Exception;

    List<ConnectionsListDTO> getListOfConnections(String laboratoryName);

    List<PrescriptionDTOToViewAsList> getAllPrescriptionTOMyPatient(String laboratoryName
            , String patientName) throws Exception;

    List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByID(String pharmacistName
            , String patientName) throws Exception;

    List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByDoctorName(
            String laboratoryName, String patientName) throws Exception;

    List<TestsInPrescriptionDTO> getPrescription(Long id, String laboratoryName);

    String addTestResult(TestsResultDTO testsResultDTO, String laboratoryName);
}
