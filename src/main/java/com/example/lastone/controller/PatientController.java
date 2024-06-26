package com.example.lastone.controller;

import com.example.lastone.model.dto.*;
import com.example.lastone.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;

    public String getPatientUserName() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @GetMapping("/get-list-of-connections")
    public List<ConnectionsListDTO> getListOfConnections() {
        return patientService.getListOfConnections();
    }

    @GetMapping("have_access")
    public Boolean haveAccess(@RequestParam String doctorName) {
        String username = getPatientUserName();
        return patientService.haveAccess(username, doctorName);
    }

    @GetMapping("/remove_access")
    public String removeAccess(@RequestParam(name = "doctorName") String doctorName) throws Exception {
        return patientService.removeDoctorAccess(doctorName);
    }

    @GetMapping("/accept_access")
    public String acceptAccess(@RequestParam String doctorName) throws Exception {
        return patientService.acceptDoctorAccess(doctorName);
    }

    @GetMapping("give_access")
    public String giveAccess(@RequestParam String doctorName) throws Exception {
        return patientService.giveAccessToDoctor(doctorName);
    }


    @GetMapping("/get-all-prescriptions")
    public List<PrescriptionDTOToViewAsList> getAllPrescriptions() {
        return patientService.getAllPrescriptionsV2(getPatientUserName());
    }

    @GetMapping("/get-prescription-details")
    public PrescriptionViewDTO getPrescription(@RequestParam Long ID) throws Exception {
        return patientService.getPrescriptionsById(ID);
    }

    @GetMapping("/give-XRayLab-access")
    public String giveAccessXRayLaboratory(String xRayLaboratoryName) throws Exception {
        return patientService.giveAccessXRayLaboratory(xRayLaboratoryName);
    }

    @GetMapping("/accept-XRayLab-access")
    public String acceptXRayLaboratoryAccess(String xRayLaboratoryName) throws Exception {
        return patientService.acceptXRayLaboratoryAccess(xRayLaboratoryName);
    }

    @GetMapping("/remove-XRayLab-access")
    public String removeXRayLaboratoryAccess(String xRayLaboratoryName) throws Exception {
        return patientService.removeXRayLaboratoryAccess(xRayLaboratoryName);
    }

    @GetMapping("/give-testLab-access")
    public String giveAccessTestsLaboratory(String laboratoryName) throws Exception {
        return patientService.giveAccessTestsLaboratory(laboratoryName);
    }

    @GetMapping("/accept-testLab-access")
    public String acceptTestsLabAccess(String laboratoryName) throws Exception {
        return patientService.acceptTestsLabAccess(laboratoryName);
    }

    @GetMapping("/remove-testLab-access")
    public String removeTestsLabAccess(String laboratoryName) throws Exception {
        return patientService.removeTestsLabAccess(laboratoryName);
    }

    @GetMapping("/give-pharmacy-access")
    public String giveAccessPharmacy(String pharmacistName) throws Exception {
        return patientService.giveAccessPharmacy(pharmacistName);
    }

    @GetMapping("/accept-pharmacy-access")
    public String acceptPharmacyAccess(String pharmacistName) throws Exception {
        return patientService.acceptPharmacyAccess(pharmacistName);
    }

    @GetMapping("/remove-pharmacy-access")
    public String removePharmacyAccess(String pharmacistName) throws Exception {
        return patientService.removePharmacyAccess(pharmacistName);
    }

    @GetMapping("/get-list-of-XRays")
    public List<ResponseAsListDTO> getListOfXRay() {
        return patientService.getListOfXRay();
    }

    @GetMapping("/get-XRay-picture")
    public ResponseEntity<?> getXRayPicture(@RequestParam Long ID) {
        byte image[] = patientService.getXRayPicture(ID);
        return ResponseEntity.status(HttpStatus.SC_OK).contentType(MediaType.valueOf("image/png")
        ).body(image);
    }

    @GetMapping("/get-list-of-tests")
    public List<ResponseAsListDTO> getListOfTests() {
        return patientService.getListOfTests();
    }

    @GetMapping("/get-test-details")
    public TestsResultDTO getTestDetails(@RequestParam Long ID) {
        return patientService.getTestDetails(ID);
    }
}
