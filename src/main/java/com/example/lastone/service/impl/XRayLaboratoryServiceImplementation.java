package com.example.lastone.service.impl;

import com.example.lastone.model.dto.PrescriptionViewDTO;
import com.example.lastone.model.entity.XRayLaboratoryPatientEntity;
import com.example.lastone.model.mapper.PrescriptionMapperIm;
import com.example.lastone.repository.PatientRepo;
import com.example.lastone.repository.PrescriptionRepo;
import com.example.lastone.repository.XRayLaboratoryPatientRepo;
import com.example.lastone.service.XRayLaboratoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class XRayLaboratoryServiceImplementation implements XRayLaboratoryService {
    private final XRayLaboratoryPatientRepo xRayLaboratoryPatientRepo;
    private final PatientRepo patientRepo;
    private final PrescriptionRepo prescriptionRepo;
    private final PrescriptionMapperIm prescriptionMapper;

    @Override
    public Boolean acceptAccess(String xRayLaboratoryName, String patientName) throws Exception {

        Optional<XRayLaboratoryPatientEntity> xRayLaboratoryPatientEntity = xRayLaboratoryPatientRepo.
                findByLaboratoryNameAndPatientName(xRayLaboratoryName, patientName);
        if (xRayLaboratoryPatientEntity.isPresent()) {
            if (!xRayLaboratoryPatientEntity.get().getAccess()) {
                xRayLaboratoryPatientEntity.get().setAccess(true);
                xRayLaboratoryPatientRepo.save(xRayLaboratoryPatientEntity.get());
                return true;
            } else {
                throw new Exception("Already have access!");
            }
        }


        throw new Exception("No Request to be accept");
    }

    @Override
    public Boolean removeAccess(String xRayLaboratoryName, String patientName) throws Exception {

        Optional<XRayLaboratoryPatientEntity> xRayLaboratoryPatientEntity = xRayLaboratoryPatientRepo.
                findByLaboratoryNameAndPatientName(xRayLaboratoryName, patientName);
        if (xRayLaboratoryPatientEntity.isPresent()) {
            xRayLaboratoryPatientRepo.
                    deleteById(xRayLaboratoryPatientEntity.get().getXRaysLaboratoryPatientsId());
            return true;
        }
        throw new Exception("Not Found!");
    }

    @Override
    public Boolean getAccess(String xRayLaboratoryName, String patientName) throws Exception {
        if (patientRepo.existsById(patientName)) {
            Optional<XRayLaboratoryPatientEntity> xRayLaboratoryPatientEntity = xRayLaboratoryPatientRepo.
                    findByLaboratoryNameAndPatientName(xRayLaboratoryName, patientName);
            if (xRayLaboratoryPatientEntity.isEmpty()) {
                XRayLaboratoryPatientEntity xRayLaboratoryPatient = new XRayLaboratoryPatientEntity();
                xRayLaboratoryPatient.setPatientName(patientName);
                xRayLaboratoryPatient.setLaboratoryName(xRayLaboratoryName);
                xRayLaboratoryPatient.setAccess(false);
                xRayLaboratoryPatientRepo.save(xRayLaboratoryPatient);
                return true;
            }
            if (xRayLaboratoryPatientEntity.get().getAccess()) {
                throw new Exception("Already have access!");
            }
            throw new Exception("Already request access");
        }
        throw new Exception("No patient with this UserName");
    }

    @Override
    public List<PrescriptionViewDTO> getAllPrescriptionTOMyPatient(String xRayLaboratoryName, String patientName) throws Exception {
        /*
        Optional<XRayLaboratoryPatientEntity> xRayLaboratoryPatient = xRayLaboratoryPatientRepo.
                findByPatientNameAndAndXRayLaboratoryName(patientName, xRayLaboratoryName);
        if (xRayLaboratoryPatient.isEmpty() || !xRayLaboratoryPatient.get().getAccess()) {
            throw new Exception("Laboratory don't have access to this patient");
        }
        List<PrescriptionEntity> prescriptionEntityList =
                prescriptionRepo.findAllByPatientName(patientName);
        List<XRayInPrescriptionDTO> prescriptionViewDTOList = new ArrayList<>();
        for (PrescriptionEntity prescription :
                prescriptionEntityList) {

        }

        return prescriptionViewDTOList;

         */
        return null;
    }
}
