package com.example.lastone.service;

import com.example.lastone.model.dto.*;

import java.io.IOException;
import java.util.List;

public interface DoctorService {


    String getAccess(String patientName) throws Exception;

    List<ConnectionsListDTO> getListOfConnections();

    PatientViewToDoctorDTO findPatient(String username) throws IOException;

    public PrescriptionViewDTO getPrescriptionTOMyPatient(Long ID, String patientName) throws Exception;

    List<PrescriptionDTOToViewAsList> getAllPrescriptionTOMyPatient(String patientName) throws Exception;

    List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByDoctorName
            (String patientName) throws Exception;

    public List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByID
            (String patientName) throws Exception;

    PrescriptionViewDTO addPrescriptionToMyPatient(PrescriptionAddFromDoctorDTO prescriptionEntity
    ) throws Exception;

    public String getUsername();

    List<PatientViewAsListDTO> getAllPatients();


    PatientViewToDoctorDTO getPatient(String patientName) throws Exception;

    Boolean haveAccess(String patientName);

    String acceptAccess(String patientName) throws Exception;

    String removeAccess(String patientName) throws Exception;


    List<ResponseAsListDTO> getListOfXRay(String patientName);

    byte[] getXRayPicture(Long id, String patientName);

    List<ResponseAsListDTO> getListOfTests(String patientName);

    TestsResultDTO getTestDetails(Long ID, String patientName);
}
