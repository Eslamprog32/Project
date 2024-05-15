package com.example.lastone.service;

import com.example.lastone.model.dto.PatientViewToDoctorDTO;
import com.example.lastone.model.dto.PrescriptionAddFromDoctorDTO;
import com.example.lastone.model.dto.PrescriptionViewDTO;

import java.util.List;

public interface DoctorService {


    Boolean getAccess(String patientName) throws Exception;

    PatientViewToDoctorDTO findPatient(String username);

    List<PrescriptionViewDTO> getAllPrescriptionTOMyPatient(String patientName) throws Exception;

    PrescriptionViewDTO addPrescriptionToMyPatient(PrescriptionAddFromDoctorDTO prescriptionEntity
    ) throws Exception;

    public String getUsername();

    List<PatientViewToDoctorDTO> getAllPatients();


    Boolean haveAccess(String patientName);

    Boolean acceptAccess(String patientName) throws Exception;

    Boolean removeAccess(String patientName) throws Exception;


}
