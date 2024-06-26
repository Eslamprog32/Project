package com.example.lastone.service;

import com.example.lastone.model.dto.*;

import java.util.List;

public interface PatientService {

    List<PrescriptionDTOToViewAsList> getAllPrescriptionsV2(String username);

    PrescriptionViewDTO getPrescriptionsById(Long id) throws Exception;

    boolean removePrescriptionById(Long id);

    Boolean haveAccess(String patientName, String doctorName);


    String acceptDoctorAccess(String doctorName) throws Exception;

    String giveAccessTestsLaboratory(String laboratoryName) throws Exception;

    String acceptXRayLaboratoryAccess(String xRayLaboratoryName) throws Exception;

    String removeDoctorAccess(String doctorName) throws Exception;

    String giveAccessToDoctor(String doctorName) throws Exception;

    List<ConnectionsListDTO> getListOfConnections();

    String removePharmacyAccess(String pharmacistName) throws Exception;

    String giveAccessXRayLaboratory(String xRayLaboratoryName) throws Exception;

    String giveAccessPharmacy(String pharmacistName) throws Exception;

    String acceptPharmacyAccess(String pharmacistName) throws Exception;

    String removeXRayLaboratoryAccess(String xRayLaboratoryName) throws Exception;


    String acceptTestsLabAccess(String laboratoryName) throws Exception;

    String removeTestsLabAccess(String laboratoryName) throws Exception;

    List<ResponseAsListDTO> getListOfXRay();

    List<ResponseAsListDTO> getListOfTests();

    TestsResultDTO getTestDetails(Long ID);

    byte[] getXRayPicture(Long id);
}
