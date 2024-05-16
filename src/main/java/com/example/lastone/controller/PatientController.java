package com.example.lastone.controller;

import com.example.lastone.model.dto.DoctorViewToPatientDTO;
import com.example.lastone.model.dto.PrescriptionDTOToViewAsList;
import com.example.lastone.model.dto.PrescriptionViewDTO;
import com.example.lastone.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @GetMapping("have_access")
    public Boolean haveAccess(@RequestParam String doctorName) {
        String username = getPatientUserName();
        return patientService.haveAccess(username, doctorName);
    }

    @GetMapping("/remove_access")
    public Boolean removeAccess(@RequestParam(name = "doctorName") String doctorName) throws Exception {
        return patientService.removeDoctorAccess(doctorName);
    }

    @GetMapping("/accept_access")
    public Boolean acceptAccess(@RequestParam String doctorName) throws Exception {
        return patientService.acceptDoctorAccess(doctorName);
    }

    @GetMapping("give_access")
    public Boolean giveAccess(@RequestParam String doctorName) throws Exception {
        return patientService.giveAccessToDoctor(doctorName);
    }

    @GetMapping("/search_for_doctor")
    public DoctorViewToPatientDTO searchForDoctor(@RequestParam String doctorName) {
        return patientService.searchForDoctor(doctorName);
    }

    @GetMapping("/get-all-doctors")
    public List<DoctorViewToPatientDTO> getAllDoctors() {
        return patientService.getAllDoctors(getPatientUserName());
    }

    @GetMapping("/get-all-prescriptions")
    public List<PrescriptionDTOToViewAsList> getAllPrescriptions() {
        return patientService.getAllPrescriptionsV2(getPatientUserName());
    }

}
