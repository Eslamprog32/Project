package com.example.lastone.service;

import com.example.lastone.model.dto.DoctorViewToPatientDTO;
import com.example.lastone.model.dto.PrescriptionDTOToViewAsList;
import com.example.lastone.model.dto.PrescriptionViewDTO;

import java.util.List;

public interface PatientService {

    List<PrescriptionDTOToViewAsList> getAllPrescriptionsV2(String username);

    PrescriptionViewDTO getPrescriptionsById(Long id) throws Exception;

    boolean removePrescriptionById(Long id);

    Boolean haveAccess(String patientName, String doctorName);


    Boolean acceptDoctorAccess(String doctorName) throws Exception;

    Boolean acceptXRayLaboratoryAccess(String doctorName) throws Exception;

    Boolean removeDoctorAccess(String doctorName) throws Exception;

    Boolean giveAccessToDoctor(String doctorName) throws Exception;

    Boolean giveAccessXRayLaboratory(String doctorName) throws Exception;

    Boolean removeXRayLaboratoryAccess(String doctorName) throws Exception;

    DoctorViewToPatientDTO searchForDoctor(String doctorName);

    List<DoctorViewToPatientDTO> getAllDoctors(String patientName);

    List<PrescriptionViewDTO> getAllPrescriptions(String username);
}
