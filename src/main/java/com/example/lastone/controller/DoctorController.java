package com.example.lastone.controller;

import com.example.lastone.model.dto.DoctorViewToPatientDTO;
import com.example.lastone.model.dto.PatientViewToDoctorDTO;
import com.example.lastone.model.dto.PrescriptionAddFromDoctorDTO;
import com.example.lastone.model.dto.PrescriptionViewDTO;
import com.example.lastone.model.entity.PrescriptionEntity;
import com.example.lastone.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping("have-access")
    public Boolean haveAccess(@RequestParam String patientName) {
        return doctorService.haveAccess(patientName);
    }

    @GetMapping("/remove_access")
    public Boolean removeAccess(@RequestParam String patient) throws Exception {
        return doctorService.removeAccess(patient);
    }
    @GetMapping("/accept_access")
    public Boolean acceptAccess(@RequestParam String patient) throws Exception {
        return doctorService.acceptAccess(patient);
    }

    @GetMapping("get_access")
    public Boolean getAccess(@RequestParam String patientName) throws Exception {
        return doctorService.getAccess(patientName);
    }

    @GetMapping("/get_all_patients")
    public List<PatientViewToDoctorDTO> getAllPatients() {
        return doctorService.getAllPatients();
    }

    @PostMapping("/add-prescription-to-my-patient")
    public PrescriptionViewDTO addPrescriptionToMyPatient(@RequestBody PrescriptionAddFromDoctorDTO
                                                                 prescriptionEntity) throws Exception {
        return doctorService.addPrescriptionToMyPatient(prescriptionEntity);
    }
    public String getDoctorUserName() {
        return doctorService.getUsername();
    }

    @GetMapping("/search_for_patient")
    public PatientViewToDoctorDTO findPatient(@RequestParam String username) {
        return doctorService.findPatient(username);
    }

}
