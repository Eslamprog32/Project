package com.example.lastone.controller;

import com.example.lastone.model.dto.ConnectionsListDTO;
import com.example.lastone.model.dto.MedicineDTO;
import com.example.lastone.model.dto.PrescriptionDTOToViewAsList;
import com.example.lastone.model.dto.XRayInPrescriptionDTO;
import com.example.lastone.service.PharmacyService;
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
@RequestMapping("/Pharmacy")
public class PharmacyController {
    private final PharmacyService pharmacyService;

    @GetMapping("/accept-access")
    public String acceptAccess(@RequestParam String pharmacistName, @RequestParam String patientName) throws Exception {
        return pharmacyService.acceptAccess(pharmacistName,
                patientName);
    }

    @GetMapping("/remove-access")
    public String removeAccess(@RequestParam String pharmacistName, @RequestParam String patientName) throws Exception {
        return pharmacyService.removeAccess(pharmacistName,
                patientName);
    }

    @GetMapping("/request-access")
    public String getAccess(@RequestParam String pharmacistName, @RequestParam String patientName) throws Exception {
        return pharmacyService.getAccess(pharmacistName, patientName);
    }

    @GetMapping("/get-all-prescriptions-sorted-by-Date")
    public List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByID(@RequestParam String pharmacistName
            , @RequestParam String patientName) throws Exception {
        return pharmacyService.getAllPrescriptionSortedByID(pharmacistName,
                patientName);
    }

    @GetMapping("/get-all-prescriptions-sorted-by-DoctorName")
    public List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByDoctorName(
            @RequestParam String pharmacistName,
            @RequestParam String patientName) throws Exception {
        return pharmacyService.
                getAllPrescriptionSortedByDoctorName(pharmacistName, patientName);
    }

    @GetMapping("/get-prescription")
    public List<MedicineDTO> getPrescription(@RequestParam Long ID,
                                             @RequestParam String pharmacistName) {
        return pharmacyService.getPrescription(ID, pharmacistName);
    }



    @GetMapping("/get-list-of-connections")
    public List<ConnectionsListDTO> getListOfConnections(String pharmacistName) {
        return pharmacyService.getListOfConnections(pharmacistName);
    }
}
