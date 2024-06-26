package com.example.lastone.controller;

import com.example.lastone.model.dto.ConnectionsListDTO;
import com.example.lastone.model.dto.PrescriptionDTOToViewAsList;
import com.example.lastone.model.dto.XRayInPrescriptionDTO;
import com.example.lastone.service.XRayLaboratoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/XRay-Lab")
public class XRayLabController {
    private final XRayLaboratoryService xRayLaboratoryService;

    @GetMapping("/accept-access")
    public String acceptAccess(@RequestParam String xRayLaboratoryName, @RequestParam String patientName) throws Exception {
        return xRayLaboratoryService.acceptAccess(xRayLaboratoryName,
                patientName);
    }

    @GetMapping("/remove-access")
    public String removeAccess(@RequestParam String xRayLaboratoryName, @RequestParam String patientName) throws Exception {
        return xRayLaboratoryService.removeAccess(xRayLaboratoryName,
                patientName);
    }

    @GetMapping("/request-access")
    public String getAccess(@RequestParam String xRayLaboratoryName, @RequestParam String patientName) throws Exception {
        return xRayLaboratoryService.getAccess(xRayLaboratoryName, patientName);
    }

    @GetMapping("/get-all-prescriptions-sorted-by-Date")
    public List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByID(@RequestParam String xRayLaboratoryName
            , @RequestParam String patientName) throws Exception {
        return xRayLaboratoryService.getAllPrescriptionSortedByID(xRayLaboratoryName,
                patientName);
    }

    @GetMapping("/get-all-prescriptions-sorted-by-DoctorName")
    public List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByDoctorName(
            @RequestParam String xRayLaboratoryName,
            @RequestParam String patientName) throws Exception {
        return xRayLaboratoryService.
                getAllPrescriptionSortedByDoctorName(xRayLaboratoryName, patientName);
    }

    @GetMapping("/get-prescription")
    public List<XRayInPrescriptionDTO> getPrescription(@RequestParam Long ID,
                                                       @RequestParam String xRayLaboratoryName) {
        return xRayLaboratoryService.getPrescription(ID, xRayLaboratoryName);
    }

    @PostMapping("/add-xRay-result")
    public String addXRayResult(@RequestParam String xRayLaboratoryName, @RequestParam String patientName
            , @RequestParam String category, @RequestParam("image") MultipartFile file) throws IOException {
        return xRayLaboratoryService.addXRayResult(xRayLaboratoryName
                , patientName, category, file);
    }

    @GetMapping("/get-list-of-connections")
    public List<ConnectionsListDTO> getListOfConnections(String xRayLaboratoryName) {
        return xRayLaboratoryService.getListOfConnections(xRayLaboratoryName);
    }
}
