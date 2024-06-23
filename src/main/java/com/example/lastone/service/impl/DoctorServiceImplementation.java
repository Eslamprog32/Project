package com.example.lastone.service.impl;

import com.example.lastone.model.dto.*;
import com.example.lastone.model.entity.*;
import com.example.lastone.model.mapper.PrescriptionMapperIm;
import com.example.lastone.model.mapper.UserMapper;
import com.example.lastone.repository.*;
import com.example.lastone.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DoctorServiceImplementation implements DoctorService   {
    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PrescriptionMapperIm prescriptionMapper;
    private final TestsInPrescriptionRepo testsInPrescriptionRepo;
    private final XRayInPrescriptionRepo xRayInPrescriptionRepo;
    private final MedicineInPrescriptionRepo medicineInPrescriptionRepo;
    private final DoctorPatientRepo doctorPatientRepo;
    private final PrescriptionRepo prescriptionRepo;

    @Override
    public String getUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @Override
    public List<PatientViewToDoctorDTO> getAllPatients() {
        Optional<DoctorEntity> doctor = doctorRepo.findById(getUsername());
        List<DoctorPatientEntity> doctorPatientEntities = doctor.get().getDoctorPatientEntities();
        List<PatientViewToDoctorDTO> result = new ArrayList<>();
        for (DoctorPatientEntity doctorPatient : doctorPatientEntities) {
            result.add(userMapper.toPatientViewToDoctorDTO(userRepo.findById(doctorPatient.getPatientName()).get()));
        }
        return result;
    }

    @Override
    public Boolean haveAccess(String patientName) {
        Optional<DoctorPatientEntity> entityDP = doctorPatientRepo.findByDoctorNameAndPatientName(getUsername(), patientName);
        if (entityDP.isPresent()) {
            return entityDP.get().getAccess();
        } else {
            throw new NoSuchElementException("Doctor_Patient Entity not found");
        }
    }

    @Override
    public Boolean acceptAccess(String patientName) throws Exception {
        Optional<DoctorPatientEntity> doctorPatient = doctorPatientRepo.
                findByDoctorNameAndPatientName(getUsername(), patientName);
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
    public Boolean removeAccess(String patientName) throws Exception {
        Optional<DoctorPatientEntity> doctorPatient = doctorPatientRepo
                .findByDoctorNameAndPatientName(getUsername(), patientName);
        if (doctorPatient.isPresent()) {
            doctorPatientRepo.deleteById(doctorPatient.get().getDoctorPatientId());
            return true;
        }
        throw new Exception("Not Found!");
    }

    @Override
    public Boolean getAccess(String patientName) throws Exception {
        if (patientRepo.existsById(patientName)) {
            Optional<DoctorPatientEntity> doctorPatient = doctorPatientRepo.
                    findByDoctorNameAndPatientName(getUsername(), patientName);
            if(!doctorPatient.isPresent()){
                DoctorPatientEntity doctorPatientEntity = new DoctorPatientEntity();
                doctorPatientEntity.setPatientName(patientName);
                doctorPatientEntity.setDoctorName(getUsername());
                doctorPatientEntity.setAccess(false);
                doctorPatientRepo.save(doctorPatientEntity);
                return true;
            }
            if(doctorPatient.get().getAccess()){
                throw new Exception("Already have access!");
            }
            throw new Exception("Already request access!");
        }
        throw new Exception("No patient with this UserName!");
    }
    @Override
    public PatientViewToDoctorDTO findPatient(String username) {
        Optional<PatientEntity> patientEntity = patientRepo.findById(username);
        if (patientEntity.isEmpty()) {
            throw new NoSuchElementException("Patient Not Found!");
        }
        return userMapper.toPatientViewToDoctorDTO(userRepo.findById(username).get());
    }

    @Override
    public List<PrescriptionViewDTO> getAllPrescriptionTOMyPatient(String patientName) throws Exception {
        if (!doctorPatientRepo.existsByDoctorNameAndPatientName(
                getUsername(), patientName)
                || !doctorPatientRepo.
                findByDoctorNameAndPatientName(getUsername(),
                        patientName).get().getAccess()) {

            throw new Exception("Doctor don't have access to this patient");
        }
        List<PrescriptionEntity> prescriptionEntityList =
                prescriptionRepo.findAllByPatientName(patientName);
        List<PrescriptionViewDTO> prescriptionViewDTOList = new ArrayList<>();
        for (PrescriptionEntity prescription :
                prescriptionEntityList) {
            prescriptionViewDTOList.
                    add(prescriptionMapper.toPrescriptionViewDTO(prescription));
        }
        return prescriptionViewDTOList;
    }

    @Override
    public PrescriptionViewDTO addPrescriptionToMyPatient(PrescriptionAddFromDoctorDTO prescriptionEntity) throws Exception {
        if (!doctorPatientRepo.existsByDoctorNameAndPatientName(
                getUsername(), prescriptionEntity.getPatientName())
                || !doctorPatientRepo.
                findByDoctorNameAndPatientName(getUsername(),
                        prescriptionEntity.getPatientName()).get().getAccess()) {

            throw new Exception("Doctor don't have access to this patient");
        }
        PrescriptionEntity prescription = new PrescriptionEntity();
        prescription.setNote(prescriptionEntity.getNote());
        prescription.setDoctorName(getUsername());
        prescription.setPatientName(prescriptionEntity.getPatientName());
        prescription.setDiagnosis(prescriptionEntity.getDiagnosis());
        prescription = prescriptionRepo.save(prescription);
        for (MedicineDTO medicine : prescriptionEntity.getMedicines()) {
            MedicineInPrescriptionEntity medicineEntity = new MedicineInPrescriptionEntity();
            medicineEntity.setPrescriptionId(prescription.getId());
            medicineEntity.setMedicine(medicine.getMedicine());
            medicineEntity.setNote(medicine.getNote());
            medicineInPrescriptionRepo.save(medicineEntity);
        }
        for (XRayInPrescriptionDTO xRayInPrescriptionDTO : prescriptionEntity.getXrayes()) {
            XRayInPrescriptionEntity xRayInPrescription = new XRayInPrescriptionEntity();
            xRayInPrescription.setPrescriptionId(prescription.getId());
            xRayInPrescription.setXRay(xRayInPrescriptionDTO.getXray());
            xRayInPrescriptionRepo.save(xRayInPrescription);
        }
        for (TestsInPrescriptionDTO tests : prescriptionEntity.getTests()) {
            TestsInPrescriptionEntity testsEntity = new TestsInPrescriptionEntity();
            testsEntity.setPrescriptionId(prescription.getId());
            testsEntity.setTest(tests.getTest());
            testsEntity.setNote(tests.getNote());
            testsInPrescriptionRepo.save(testsEntity);
        }
        return new PrescriptionViewDTO(prescription.getId(), prescription.getPatientName(),
                prescription.getDoctorName(), prescriptionEntity.getMedicines(), prescriptionEntity.getXrayes(),
                prescriptionEntity.getTests(),
                prescriptionEntity.getNote(), prescriptionEntity.getDiagnosis()
                , prescription.getCreatedAt());
    }

}
