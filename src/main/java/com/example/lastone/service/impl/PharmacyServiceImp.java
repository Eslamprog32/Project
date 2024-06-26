package com.example.lastone.service.impl;

import com.example.lastone.Exceptions.MessageError;
import com.example.lastone.model.dto.ConnectionsListDTO;
import com.example.lastone.model.dto.MedicineDTO;
import com.example.lastone.model.dto.PrescriptionDTOToViewAsList;
import com.example.lastone.model.entity.*;
import com.example.lastone.model.mapper.PrescriptionMapperIm;
import com.example.lastone.repository.*;
import com.example.lastone.service.PharmacyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PharmacyServiceImp implements PharmacyService {
    private final OrganizationRepo organizationRepo;
    private final PharmacistRepo pharmacistRepo;
    private final PharmacistPatientRepo pharmacistPatientRepo;
    private final UserRepo userRepo;
    private final WorksInRepo worksInRepo;
    private final PatientRepo patientRepo;
    private final PrescriptionRepo prescriptionRepo;
    private final PrescriptionMapperIm prescriptionMapper;
    private final MedicineInPrescriptionRepo medicineInPrescriptionRepo;

    private String getUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @Override
    public int canAccessThisLab(String organizationName) {
        if (!pharmacistRepo.existsById(organizationName)) {
            throw new MessageError("No Pharmacy With This Name");
        }
        Optional<WorksInEntity> works = worksInRepo.
                findByUsernameAndOrganizationName(getUsername(), organizationName);
        if (works.isPresent()) {
            if (works.get().getAdmin()) {
                return 2;
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }

    @Override
    public String getAccess(String pharmacistName, String patientName) throws Exception {
        if (canAccessThisLab(pharmacistName) == 0) {
            throw new MessageError("User Can't Access This Pharmacy");
        }
        if (patientRepo.existsById(patientName)) {
            Optional<PharmacistPatientEntity> pharmacistPatientEntity = pharmacistPatientRepo.
                    findByPharmacistNameAndPatientName(pharmacistName, patientName);
            if (pharmacistPatientEntity.isEmpty()) {
                PharmacistPatientEntity pharmacistPatient = new PharmacistPatientEntity();
                pharmacistPatient.setPatientName(patientName);
                pharmacistPatient.setPharmacistName(pharmacistName);
                pharmacistPatient.setAccess(false);
                pharmacistPatient.setWhichRequestAccess(false);
                pharmacistPatientRepo.save(pharmacistPatient);
                return "Request Access Done";
            }
            if (pharmacistPatientEntity.get().getAccess()) {
                throw new MessageError("Already have access!");
            }
            throw new MessageError("Already request access");
        }
        throw new MessageError("No patient with this UserName");
    }

    @Override
    public String acceptAccess(String pharmacistName, String patientName) throws Exception {
        if (canAccessThisLab(pharmacistName) == 0) {
            throw new MessageError("User Can't Access This Pharmacy");
        }
        Optional<PharmacistPatientEntity> pharmacistPatientEntity = pharmacistPatientRepo.
                findByPharmacistNameAndPatientName(pharmacistName, patientName);

        if (pharmacistPatientEntity.isPresent()) {
            if (!pharmacistPatientEntity.get().getAccess() && pharmacistPatientEntity.get().getWhichRequestAccess()) {
                pharmacistPatientEntity.get().setAccess(true);
                pharmacistPatientRepo.save(pharmacistPatientEntity.get());
                return "Done Access Accept";
            } else {
                throw new MessageError("Already have access!");
            }
        }
        throw new MessageError("No Request to be accept");
    }

    @Override
    public String removeAccess(String pharmacistName, String patientName) throws Exception {
        if (canAccessThisLab(pharmacistName) == 0) {
            throw new MessageError("User Can't Access This Pharmacy");
        }
        Optional<PharmacistPatientEntity> xRayLaboratoryPatientEntity = pharmacistPatientRepo.
                findByPharmacistNameAndPatientName(pharmacistName, patientName);
        if (xRayLaboratoryPatientEntity.isPresent()) {
            pharmacistPatientRepo.deleteById(xRayLaboratoryPatientEntity.get().getPharmacistPatientID());
            return "Done Access Removed";
        }
        throw new MessageError("Not Found!");
    }

    @Override
    public List<ConnectionsListDTO> getListOfConnections(String pharmacistName) {
        if (canAccessThisLab(pharmacistName) == 0) {
            throw new MessageError("User Can't Access This Pharmacy");
        }
        List<PharmacistPatientEntity> pharmacistPatientEntityList =
                pharmacistPatientRepo.findAllByPharmacistName(pharmacistName);
        List<ConnectionsListDTO> connectionsListDTO = new ArrayList<>();
        for (PharmacistPatientEntity pharmacistPatientEntity :
                pharmacistPatientEntityList) {
            if (pharmacistPatientEntity.getAccess()) {
                connectionsListDTO.add(new ConnectionsListDTO(pharmacistPatientEntity.getPatientName()
                        , userRepo.findByUsername(
                        pharmacistPatientEntity.getPatientName()).get().getFullName()
                        , true, true));
            } else if (pharmacistPatientEntity.getWhichRequestAccess()) {
                connectionsListDTO.add(new ConnectionsListDTO(pharmacistPatientEntity.getPatientName()
                        , userRepo.findByUsername(
                        pharmacistPatientEntity.getPatientName()).get().getFullName()
                        , false, true));
            }
        }
        return connectionsListDTO;
    }


    @Override
    public List<PrescriptionDTOToViewAsList> getAllPrescriptionTOMyPatient(String pharmacistName
            , String patientName) throws Exception {
        if (canAccessThisLab(pharmacistName) == 0) {
            throw new MessageError("User Can't Access This Pharmacy");
        }
        Optional<PharmacistPatientEntity> pharmacistPatient = pharmacistPatientRepo.
                findByPharmacistNameAndPatientName(pharmacistName, patientName);
        if (pharmacistPatient.isEmpty() || !pharmacistPatient.get().getAccess()) {
            throw new MessageError("Pharmacy don't have access to this patient");
        }
        List<PrescriptionEntity> prescriptionEntityList = prescriptionRepo.
                findAllByPatientName(patientName);
        List<PrescriptionDTOToViewAsList> prescriptionViewDTOList = new ArrayList<>();
        for (PrescriptionEntity prescription : prescriptionEntityList) {
            prescriptionViewDTOList.add(prescriptionMapper.toPrescriptionDTOToViewAsList(prescription));
        }
        prescriptionViewDTOList.sort((o1, o2) -> o1.getPrescription_id().compareTo(o2.getPrescription_id()) * -1);
        return prescriptionViewDTOList;
    }


    @Override
    public List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByID(String pharmacistName
            , String patientName) throws Exception {
        return getAllPrescriptionTOMyPatient(pharmacistName, patientName);
    }

    @Override
    public List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByDoctorName(
            String pharmacistName, String patientName) throws Exception {
        List<PrescriptionDTOToViewAsList> result = getAllPrescriptionTOMyPatient(pharmacistName
                , patientName);
        result.sort((o1, o2) -> o1.getDoctorName().compareTo(o2.getDoctorName()) * -1);
        return result;
    }

    @Override
    public List<MedicineDTO> getPrescription(Long id, String pharmacistName) {
        if (canAccessThisLab(pharmacistName) == 0) {
            throw new MessageError("User Can't Access This Pharmacy");
        }
        List<MedicineDTO> medicineDTOList = new ArrayList<>();
        Optional<PrescriptionEntity> prescription = prescriptionRepo.findById(id);
        if (prescription.isEmpty()) {
            throw new MessageError("No prescription with this id");
        }
        Optional<PharmacistPatientEntity> pharmacistPatient = pharmacistPatientRepo.
                findByPharmacistNameAndPatientName(pharmacistName, prescription.get().getPatientName());
        if (pharmacistPatient.isEmpty() || !pharmacistPatient.get().getAccess()) {
            throw new MessageError("Pharmacy didn't have access to this patient");
        }
        List<MedicineInPrescriptionEntity> medicine = medicineInPrescriptionRepo.
                findAllByPrescriptionId(id);
        for (MedicineInPrescriptionEntity medicine1 : medicine) {
            medicineDTOList.add(new MedicineDTO(
                    medicine1.getMedicine(), medicine1.getNote()));
        }
        return medicineDTOList;
    }

}
