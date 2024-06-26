package com.example.lastone.controller;

import com.example.lastone.model.dto.*;
import com.example.lastone.service.TestsLabService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Test-Lab")
public class TestsLabController {
    private final TestsLabService testsLabService;

    @GetMapping("/accept-access")
    public String acceptAccess(@RequestParam String laboratoryName, @RequestParam String patientName) throws Exception {
        return testsLabService.acceptAccess(laboratoryName,
                patientName);
    }

    @GetMapping("/remove-access")
    public String removeAccess(@RequestParam String laboratoryName, @RequestParam String patientName) throws Exception {
        return testsLabService.removeAccess(laboratoryName,
                patientName);
    }

    @GetMapping("/request-access")
    public String getAccess(@RequestParam String laboratoryName, @RequestParam String patientName) throws Exception {
        return testsLabService.getAccess(laboratoryName, patientName);
    }

    @GetMapping("/get-all-prescriptions-sorted-by-Date")
    public List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByID(@RequestParam String laboratoryName
            , @RequestParam String patientName) throws Exception {
        return testsLabService.getAllPrescriptionSortedByID(laboratoryName,
                patientName);
    }

    @GetMapping("/get-all-prescriptions-sorted-by-DoctorName")
    public List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByDoctorName(
            @RequestParam String laboratoryName,
            @RequestParam String patientName) throws Exception {
        return testsLabService.
                getAllPrescriptionSortedByDoctorName(laboratoryName, patientName);
    }

    @GetMapping("/get-prescription")
    public List<TestsInPrescriptionDTO> getPrescription(@RequestParam Long ID,
                                                        @RequestParam String laboratoryName) {
        return testsLabService.getPrescription(ID, laboratoryName);
    }


    @GetMapping("/get-list-of-connections")
    public List<ConnectionsListDTO> getListOfConnections(String laboratoryName) {
        return testsLabService.getListOfConnections(laboratoryName);
    }

    @PostMapping("/add-test-result")
    public String addTestResult(@RequestParam String laboratoryName,@RequestBody TestsResultDTO testsResultDTO) {
        return testsLabService.addTestResult(testsResultDTO, laboratoryName);
    }

}
