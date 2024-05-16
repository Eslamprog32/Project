package com.example.lastone.service.impl;

import com.example.lastone.model.dto.*;
import com.example.lastone.model.entity.*;
import com.example.lastone.model.mapper.PrescriptionMapperIm;
import com.example.lastone.model.mapper.UserMapper;
import com.example.lastone.repository.*;
import com.example.lastone.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PatientServiceImplementation implements PatientService {
    private final DoctorPatientRepo doctorPatientRepo;
    private final DoctorRepo doctorRepo;
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PrescriptionRepo prescriptionRepo;
    private final PrescriptionMapperIm prescriptionMapper;
    private final XRayLaboratoryRepo xRayLaboratoryRepo;
    private final XRayLaboratoryPatientRepo xRayLaboratoryPatientRepo;

    public String getPatientUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Override
    public List<DoctorViewToPatientDTO> getAllDoctors(String patientName) {
        List<DoctorPatientEntity> doctorPatient = doctorPatientRepo.findAllByPatientName(patientName);
        List<DoctorViewToPatientDTO> result = new ArrayList<>();
        for (DoctorPatientEntity doctorPatient1 : doctorPatient) {
            DoctorViewToPatientDTO doctorViewToPatientDTO = userMapper.toDoctorViewToPatientDTO(userRepo.findById(doctorPatient1.getDoctorName()).get());
            doctorViewToPatientDTO.setSpecialization(doctorRepo.findById(doctorPatient1.getDoctorName()).get().getSpecialization());
            result.add(doctorViewToPatientDTO);
        }
        return result;
    }

    @Override
    public List<PrescriptionViewDTO> getAllPrescriptions(String username) {
        List<PrescriptionEntity> prescriptionEntities = prescriptionRepo.findAllByPatientName(username);
        List<PrescriptionViewDTO> prescriptionViewDTOList = new ArrayList<>();
        for (PrescriptionEntity prescriptionEntity : prescriptionEntities) {
            prescriptionViewDTOList.add(prescriptionMapper.toPrescriptionViewDTO(prescriptionEntity));
        }
        return prescriptionViewDTOList;
    }
    @Override
    public List<PrescriptionDTOToViewAsList> getAllPrescriptionsV2(String username) {
        return prescriptionRepo.findAllByPatientName2(username);
    }

    @Override
    public PrescriptionViewDTO getPrescriptionsById(Long id) throws Exception {
        Optional<PrescriptionEntity> prescriptionEntities =
                prescriptionRepo.findByPatientNameAndId(getPatientUserName()
                        , id);
        if (prescriptionEntities.isPresent()) {
            return prescriptionMapper.toPrescriptionViewDTO(prescriptionEntities.get());
        } else {
            throw new Exception(("Prescription Not Found!"));
        }
    }

    @Override
    public boolean removePrescriptionById(Long id) {
        if (prescriptionRepo.existsByPatientNameAndId(getPatientUserName(), id)) {
            prescriptionRepo.deleteByPatientNameAndId(getPatientUserName(), id);
            return true;
        }
        return false;
    }

    @Override
    public Boolean haveAccess(String patientName, String doctorName) {
        Optional<DoctorPatientEntity> entityDP = doctorPatientRepo.findByDoctorNameAndPatientName(doctorName, patientName);
        if (entityDP.isPresent() && entityDP.get().getAccess() != null) {
            return entityDP.get().getAccess();
        } else {
            return false;
        }

    }

    @Override
    public Boolean acceptDoctorAccess(String doctorName) throws Exception {
        Optional<DoctorPatientEntity> doctorPatient = doctorPatientRepo.
                findByDoctorNameAndPatientName(doctorName, getPatientUserName());
        if (doctorPatient.isPresent()) {
            if (!doctorPatient.get().getAccess()) {
                doctorPatient.get().setAccess(true);
                doctorPatientRepo.save(doctorPatient.get());
                return true;
            } else {
                throw new Exception("Already have access!");
            }
        }
        throw new Exception("No Request to be accept");
    }

    @Override
    public Boolean removeDoctorAccess(String doctorName) throws Exception {
        Optional<DoctorPatientEntity> doctorPatient = doctorPatientRepo
                .findByDoctorNameAndPatientName(doctorName, getPatientUserName());

        if (doctorPatient.isPresent()) {
            doctorPatientRepo.deleteById(doctorPatient.get().getDoctorPatientId());
            return true;
        }
        throw new Exception("Not Found!");
    }

    @Override
    public Boolean giveAccessToDoctor(String doctorName) throws Exception {
        if (doctorRepo.existsByDoctorName(doctorName)) {
            Optional<DoctorPatientEntity> doctorPatient = doctorPatientRepo.
                    findByDoctorNameAndPatientName(doctorName, getPatientUserName());
            if(doctorPatient.isEmpty()){
                DoctorPatientEntity doctorPatientEntity = new DoctorPatientEntity();
                doctorPatientEntity.setPatientName(getPatientUserName());
                doctorPatientEntity.setDoctorName(doctorName);
                doctorPatientEntity.setAccess(false);
                doctorPatientRepo.save(doctorPatientEntity);
                return true;
            }
            if(doctorPatient.get().getAccess()){
                throw new Exception("Already have access!");
            }
            throw new Exception("Already request access");
        }
        throw new Exception("No doctor with this UserName");
    }
    @Override
    public Boolean giveAccessXRayLaboratory(String xRayLaboratoryName) throws Exception {

        if (xRayLaboratoryRepo.existsById(xRayLaboratoryName)) {
            Optional<XRayLaboratoryPatientEntity> xRayLaboratoryPatientEntity = xRayLaboratoryPatientRepo.
                    findByLaboratoryNameAndPatientName(xRayLaboratoryName,getPatientUserName());
            if(xRayLaboratoryPatientEntity.isEmpty()){
                XRayLaboratoryPatientEntity xRayLaboratoryPatient = new XRayLaboratoryPatientEntity();
                xRayLaboratoryPatient.setPatientName(getPatientUserName());
                xRayLaboratoryPatient.setLaboratoryName(xRayLaboratoryName);
                xRayLaboratoryPatient.setAccess(false);
                xRayLaboratoryPatientRepo.save(xRayLaboratoryPatient);
                return true;
            }
            if(xRayLaboratoryPatientEntity.get().getAccess()){
                throw new Exception("Already have access!");
            }
            throw new Exception("Already request access");
        }
        throw new Exception("No xRayLaboratory with this UserName");
    }
    @Override
    public Boolean acceptXRayLaboratoryAccess(String xRayLaboratoryName) throws Exception {

        Optional<XRayLaboratoryPatientEntity> xRayLaboratoryPatientEntity = xRayLaboratoryPatientRepo.
                findByLaboratoryNameAndPatientName(xRayLaboratoryName,getPatientUserName());
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
    public Boolean removeXRayLaboratoryAccess(String xRayLaboratoryName) throws Exception {

        Optional<XRayLaboratoryPatientEntity> xRayLaboratoryPatientEntity = xRayLaboratoryPatientRepo
                .findByLaboratoryNameAndPatientName(xRayLaboratoryName,getPatientUserName());
        if (xRayLaboratoryPatientEntity.isPresent()) {
            xRayLaboratoryPatientRepo.deleteById(
                    xRayLaboratoryPatientEntity.get().getXRaysLaboratoryPatientsId());
            return true;
        }
        throw new Exception("Not Found!");
    }
    @Override
    public DoctorViewToPatientDTO searchForDoctor(String doctorName) {
        Optional<DoctorEntity> doctorEntity = doctorRepo.findById(doctorName);
        if (doctorEntity.isEmpty()) {
            throw new NoSuchElementException("Doctor Not Found!");
        }
        DoctorViewToPatientDTO viewToPatientDTO = userMapper.toDoctorViewToPatientDTO(userRepo.findById(doctorName).get());
        viewToPatientDTO.setSpecialization(doctorEntity.get().getSpecialization());
        return viewToPatientDTO;
    }
}
