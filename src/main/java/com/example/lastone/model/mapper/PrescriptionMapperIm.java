package com.example.lastone.model.mapper;

import com.example.lastone.model.dto.*;
import com.example.lastone.model.entity.MedicineInPrescriptionEntity;
import com.example.lastone.model.entity.PrescriptionEntity;
import com.example.lastone.model.entity.TestsInPrescriptionEntity;
import com.example.lastone.model.entity.XRayInPrescriptionEntity;
import com.example.lastone.repository.MedicineInPrescriptionRepo;
import com.example.lastone.repository.TestsInPrescriptionRepo;
import com.example.lastone.repository.XRayInPrescriptionRepo;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PrescriptionMapperIm {
    private final MedicineInPrescriptionRepo medicineInPrescriptionRepo;
    private final TestsInPrescriptionRepo testsInPrescriptionRepo;
    private final XRayInPrescriptionRepo xRayInPrescriptionRepo;

    public PrescriptionViewDTO toPrescriptionViewDTO(PrescriptionEntity prescriptionEntity) {
        List<MedicineDTO> medicineDTOList = new ArrayList<>();
        List<MedicineInPrescriptionEntity> medicineInPrescriptionEntities =
                medicineInPrescriptionRepo.findAllByPrescriptionId(prescriptionEntity.getId());
        for (MedicineInPrescriptionEntity medicine : medicineInPrescriptionEntities) {
            medicineDTOList.add(new MedicineDTO(medicine.getMedicine(), medicine.getNote()));
        }
        List<TestsInPrescriptionDTO> testsInPrescriptionDTOList = new ArrayList<>();

        List<TestsInPrescriptionEntity> tests =
                testsInPrescriptionRepo.findAllByPrescriptionId(prescriptionEntity.getId());
        for (TestsInPrescriptionEntity testsInPrescriptionEntity : tests) {
            testsInPrescriptionDTOList.add(new TestsInPrescriptionDTO(testsInPrescriptionEntity.getTest(),
                    testsInPrescriptionEntity.getNote()));
        }
        List<XRayInPrescriptionDTO> xRayInPrescriptionDTOList = new ArrayList<>();
        List<XRayInPrescriptionEntity> xRays =
                xRayInPrescriptionRepo.findAllByPrescriptionId(prescriptionEntity.getId());
        for (XRayInPrescriptionEntity xRay : xRays) {
            xRayInPrescriptionDTOList.add(new XRayInPrescriptionDTO(xRay.getXRay()));
        }
        return new PrescriptionViewDTO(prescriptionEntity.getId()
                , prescriptionEntity.getPatientName(),
                prescriptionEntity.getDoctorName(), medicineDTOList, xRayInPrescriptionDTOList
                , testsInPrescriptionDTOList, prescriptionEntity.getNote(),
                prescriptionEntity.getDiagnosis(), prescriptionEntity.getCreatedAt());
    }
    XRayInPrescriptionDTO toXRayInPrescriptionDto(XRayInPrescriptionEntity xRayInPrescription){
        return null;
    }
}
