package com.example.lastone.controller;

import com.example.lastone.model.dto.*;
import com.example.lastone.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping("/get-list-of-connections")
    public List<ConnectionsListDTO> getListOfConnections() {
        return doctorService.getListOfConnections();
    }

    @GetMapping("have-access")
    public Boolean haveAccess(@RequestParam String patientName) {
        return doctorService.haveAccess(patientName);
    }

    @GetMapping("/remove_access")
    public String removeAccess(@RequestParam String patient) throws Exception {
        return doctorService.removeAccess(patient);
    }

    @GetMapping("/get-my-patient")
    public PatientViewToDoctorDTO getPatient(@RequestParam String patientName) throws Exception {
        return doctorService.getPatient(patientName);
    }

    @GetMapping("/accept_access")
    public String acceptAccess(@RequestParam String patient) throws Exception {
        return doctorService.acceptAccess(patient);
    }

    @GetMapping("get_access")
    public String getAccess(@RequestParam String patientName) throws Exception {
        return doctorService.getAccess(patientName);
    }

    @GetMapping("/get_all_patients")
    public List<PatientViewAsListDTO> getAllPatients() {
        return doctorService.getAllPatients();
    }

    @PostMapping("/add-prescription-to-my-patient")
    public PrescriptionViewDTO addPrescriptionToMyPatient(@RequestBody PrescriptionAddFromDoctorDTO
                                                                  prescriptionEntity) throws Exception {
        return doctorService.addPrescriptionToMyPatient(prescriptionEntity);
    }

    @GetMapping("/get-prescription")
    public PrescriptionViewDTO getPrescriptionTOMyPatient(@RequestParam Long ID, @RequestParam
    String patientName) throws Exception {
        return doctorService.getPrescriptionTOMyPatient(ID, patientName);
    }

    @GetMapping("/get-all-prescriptions-sorted-by-Date")
    public List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByID(String patientName) throws Exception {
        return doctorService.getAllPrescriptionSortedByID(patientName);
    }

    @GetMapping("/get-all-prescriptions-sorted-by-DoctorName")
    public List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByDoctorName(String patientName) throws Exception {
        return doctorService.getAllPrescriptionSortedByDoctorName(patientName);
    }

    public String getDoctorUserName() {
        return doctorService.getUsername();
    }

    @GetMapping("/get-list-of-XRays")
    public List<ResponseAsListDTO> getListOfXRay(@RequestParam String patientName) {
        return doctorService.getListOfXRay(patientName);
    }

    @GetMapping("/get-XRay-picture")
    public ResponseEntity<?> getXRayPicture(@RequestParam Long ID, @RequestParam String patientName) {
        byte image[] = doctorService.getXRayPicture(ID, patientName);
        return ResponseEntity.status(HttpStatus.SC_OK).contentType(MediaType.valueOf("image/png")
        ).body(image);
    }

    @GetMapping("/get-list-of-tests")
    public List<ResponseAsListDTO> getListOfTests(@RequestParam String patientName) {
        return doctorService.getListOfTests(patientName);
    }

    @GetMapping("/get-test-details")
    public TestsResultDTO getTestDetails(@RequestParam Long ID, @RequestParam String patientName) {
        return doctorService.getTestDetails(ID, patientName);
    }
}
