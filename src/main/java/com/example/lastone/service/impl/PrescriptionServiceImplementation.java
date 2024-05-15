package com.example.lastone.service.impl;

import com.example.lastone.model.entity.XRayInPrescriptionEntity;
import com.example.lastone.repository.MedicineInPrescriptionRepo;
import com.example.lastone.repository.XRayInPrescriptionRepo;
import com.example.lastone.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PrescriptionServiceImplementation implements PrescriptionService {

    private final MedicineInPrescriptionRepo medicineInPrescriptionRepo;
    private final XRayInPrescriptionRepo xRayRepo;


    @Override
    public boolean addXRay(XRayInPrescriptionEntity xRay) {
        xRayRepo.save(xRay);
        return true;
    }
}