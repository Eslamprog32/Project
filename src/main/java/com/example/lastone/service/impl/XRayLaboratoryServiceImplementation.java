package com.example.lastone.service.impl;

import com.example.lastone.Exceptions.MessageError;
import com.example.lastone.model.dto.ConnectionsListDTO;
import com.example.lastone.model.dto.PrescriptionDTOToViewAsList;
import com.example.lastone.model.dto.XRayInPrescriptionDTO;
import com.example.lastone.model.entity.*;
import com.example.lastone.model.mapper.PrescriptionMapperIm;
import com.example.lastone.repository.*;
import com.example.lastone.service.XRayLaboratoryService;
import com.example.lastone.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class XRayLaboratoryServiceImplementation implements XRayLaboratoryService {
    private final XRayLaboratoryPatientRepo xRayLaboratoryPatientRepo;
    private final XRayInPrescriptionRepo xRayInPrescriptionRepo;
    private final XRayLaboratoryRepo xRayLaboratoryRepo;
    private final XRayRepo xRayRepo;
    private final PatientRepo patientRepo;
    private final PrescriptionRepo prescriptionRepo;
    private final PrescriptionMapperIm prescriptionMapper;
    private final WorksInRepo worksInRepo;
    private final OrganizationRepo organizationRepo;
    private final UserRepo userRepo;

    private String getUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @Override
    public String acceptAccess(String xRayLaboratoryName, String patientName) throws Exception {
        if (canAccessThisLab(xRayLaboratoryName) == 0) {
            throw new MessageError("User Can't Access This Lab");
        }
        Optional<XRayLaboratoryPatientEntity> xRayLaboratoryPatientEntity = xRayLaboratoryPatientRepo.findByLaboratoryNameAndPatientName(xRayLaboratoryName, patientName);
        if (xRayLaboratoryPatientEntity.isPresent()) {
            if (!xRayLaboratoryPatientEntity.get().getAccess() && xRayLaboratoryPatientEntity.get().getWhichRequestAccess()) {
                xRayLaboratoryPatientEntity.get().setAccess(true);
                xRayLaboratoryPatientRepo.save(xRayLaboratoryPatientEntity.get());
                return "Accept Access Done";
            } else {
                throw new MessageError("Already have access!");
            }
        }
        throw new MessageError("No Request to be accept");
    }

    @Override
    public String removeAccess(String xRayLaboratoryName, String patientName) throws Exception {
        if (canAccessThisLab(xRayLaboratoryName) == 0) {
            throw new MessageError("User Can't Access This Lab");
        }
        Optional<XRayLaboratoryPatientEntity> xRayLaboratoryPatientEntity = xRayLaboratoryPatientRepo.findByLaboratoryNameAndPatientName(xRayLaboratoryName, patientName);
        if (xRayLaboratoryPatientEntity.isPresent()) {
            xRayLaboratoryPatientRepo.deleteById(xRayLaboratoryPatientEntity.get().getXRaysLaboratoryPatientsId());
            return "Access Removed";
        }
        throw new MessageError("Not Found!");
    }

    @Override
    public List<ConnectionsListDTO> getListOfConnections(String xRayLaboratoryName) {
        if (canAccessThisLab(xRayLaboratoryName) == 0) {
            throw new MessageError("User Can't Access This Lab");
        }
        List<XRayLaboratoryPatientEntity> xRayLaboratoryPatientEntityList =
                xRayLaboratoryPatientRepo.findAllByLaboratoryName(xRayLaboratoryName);
        List<ConnectionsListDTO> connectionsListDTO = new ArrayList<>();
        for (XRayLaboratoryPatientEntity xRayLaboratoryPatient :
                xRayLaboratoryPatientEntityList) {
            if (xRayLaboratoryPatient.getAccess()) {
                connectionsListDTO.add(new ConnectionsListDTO(xRayLaboratoryPatient.getPatientName()
                        , userRepo.findByUsername(
                        xRayLaboratoryPatient.getPatientName()).get().getFullName()
                        , true, true));
            } else if (xRayLaboratoryPatient.getWhichRequestAccess()) {
                connectionsListDTO.add(new ConnectionsListDTO(xRayLaboratoryPatient.getPatientName()
                        , userRepo.findByUsername(
                        xRayLaboratoryPatient.getPatientName()).get().getFullName()
                        , false, true));
            }
        }
        return connectionsListDTO;
    }

    @Override
    public String getAccess(String xRayLaboratoryName, String patientName) throws Exception {
        if (canAccessThisLab(xRayLaboratoryName) == 0) {
            throw new MessageError("User Can't Access This Lab");
        }
        if (patientRepo.existsById(patientName)) {
            Optional<XRayLaboratoryPatientEntity> xRayLaboratoryPatientEntity = xRayLaboratoryPatientRepo.findByLaboratoryNameAndPatientName(xRayLaboratoryName, patientName);
            if (xRayLaboratoryPatientEntity.isEmpty()) {
                XRayLaboratoryPatientEntity xRayLaboratoryPatient = new XRayLaboratoryPatientEntity();
                xRayLaboratoryPatient.setPatientName(patientName);
                xRayLaboratoryPatient.setLaboratoryName(xRayLaboratoryName);
                xRayLaboratoryPatient.setAccess(false);
                xRayLaboratoryPatient.setWhichRequestAccess(false);
                xRayLaboratoryPatientRepo.save(xRayLaboratoryPatient);
                return "Request Access Done";
            }
            if (xRayLaboratoryPatientEntity.get().getAccess()) {
                throw new MessageError("Already have access!");
            }
            throw new MessageError("Already request access");
        }
        throw new MessageError("No patient with this UserName");
    }

    @Override
    public List<PrescriptionDTOToViewAsList> getAllPrescriptionTOMyPatient(String xRayLaboratoryName, String patientName) throws Exception {
        if (canAccessThisLab(xRayLaboratoryName) == 0) {
            throw new MessageError("User Can't Access This Lab");
        }
        Optional<XRayLaboratoryPatientEntity> xrayLab = xRayLaboratoryPatientRepo.findByLaboratoryNameAndPatientName(xRayLaboratoryName, patientName);
        if (xrayLab.isEmpty() || !xrayLab.get().getAccess()) {
            throw new MessageError("Lab don't have access to this patient");
        }
        List<PrescriptionEntity> prescriptionEntityList = prescriptionRepo.findAllByPatientName(patientName);
        List<PrescriptionDTOToViewAsList> prescriptionViewDTOList = new ArrayList<>();
        for (PrescriptionEntity prescription : prescriptionEntityList) {
            prescriptionViewDTOList.add(prescriptionMapper.toPrescriptionDTOToViewAsList(prescription));
        }
        prescriptionViewDTOList.sort((o1, o2) -> o1.getPrescription_id().compareTo(o2.getPrescription_id()) * -1);
        return prescriptionViewDTOList;
    }

    @Override
    public int canAccessThisLab(String xRayLaboratoryName) {
        if (!xRayLaboratoryRepo.existsById(xRayLaboratoryName)) {
            throw new MessageError("No XRay-Lab With This Name");
        }
        Optional<WorksInEntity> works = worksInRepo.findByUsernameAndOrganizationName(getUsername(), xRayLaboratoryName);
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
    public List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByID(String xRayLaboratoryName, String patientName) throws Exception {
        return getAllPrescriptionTOMyPatient(xRayLaboratoryName, patientName);
    }

    @Override
    public List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByDoctorName(String xRayLaboratoryName, String patientName) throws Exception {
        List<PrescriptionDTOToViewAsList> result = getAllPrescriptionTOMyPatient(xRayLaboratoryName, patientName);
        result.sort((o1, o2) -> o1.getDoctorName().compareTo(o2.getDoctorName()) * -1);
        return result;
    }

    @Override
    public List<XRayInPrescriptionDTO> getPrescription(Long id, String xRayLaboratoryName) {
        if (canAccessThisLab(xRayLaboratoryName) == 0) {
            throw new MessageError("User Can't Access This Lab");
        }
        List<XRayInPrescriptionDTO> xRayInPrescriptionDTOList = new ArrayList<>();
        Optional<PrescriptionEntity> prescription = prescriptionRepo.findById(id);
        if (prescription.isEmpty()) {
            throw new MessageError("No prescription with this id");
        }
        Optional<XRayLaboratoryPatientEntity> xrayLab = xRayLaboratoryPatientRepo.findByLaboratoryNameAndPatientName(xRayLaboratoryName, prescription.get().getPatientName());
        if (xrayLab.isEmpty() || !xrayLab.get().getAccess()) {
            throw new MessageError("Lab didn't have access to this patient");
        }
        List<XRayInPrescriptionEntity> xRays = xRayInPrescriptionRepo.findAllByPrescriptionId(id);
        for (XRayInPrescriptionEntity xRay : xRays) {
            xRayInPrescriptionDTOList.add(new XRayInPrescriptionDTO(xRay.getXRay(), xRay.getNote()));
        }
        return xRayInPrescriptionDTOList;
    }

    @Override
    public String addXRayResult(String xRayLaboratoryName, String patientName, String category, MultipartFile file) throws IOException {
        if (canAccessThisLab(xRayLaboratoryName) == 0) {
            throw new MessageError("User Can't Access This Lab");
        }
        Optional<XRayLaboratoryPatientEntity> xRayLaboratoryPatient = xRayLaboratoryPatientRepo.findByLaboratoryNameAndPatientName(xRayLaboratoryName, patientName);
        if (xRayLaboratoryPatient.isEmpty() || !xRayLaboratoryPatient.get().getAccess()) {
            throw new MessageError("No Access!");
        } else {
            XRayEntity xRay = new XRayEntity();
            xRay.setPatientName(patientName);
            xRay.setPicture(ImageUtils.compressImage(file.getBytes()));
            xRay.setCategory(category);
            xRayRepo.save(xRay);
            return "Add Successfully!";
        }
    }

}
