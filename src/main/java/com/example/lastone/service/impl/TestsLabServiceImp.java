package com.example.lastone.service.impl;

import com.example.lastone.Exceptions.MessageError;
import com.example.lastone.model.dto.ConnectionsListDTO;
import com.example.lastone.model.dto.PrescriptionDTOToViewAsList;
import com.example.lastone.model.dto.TestsInPrescriptionDTO;
import com.example.lastone.model.dto.TestsResultDTO;
import com.example.lastone.model.entity.*;
import com.example.lastone.model.mapper.PrescriptionMapperIm;
import com.example.lastone.model.mapper.TestsMapper;
import com.example.lastone.repository.*;
import com.example.lastone.service.TestsLabService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestsLabServiceImp implements TestsLabService {
    private final TestsLabRepo testsLabRepo;
    private final WorksInRepo worksInRepo;
    private final PatientRepo patientRepo;
    private final TestsPatientRepo testsPatientRepo;
    private final UserRepo userRepo;
    private final PrescriptionRepo prescriptionRepo;
    private final PrescriptionMapperIm prescriptionMapper;
    private final TestsInPrescriptionRepo testsInPrescriptionRepo;
    private final TestsMapper testsMapper;
    private final TestsRepo testsRepo;

    private String getUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @Override
    public int canAccessThisLab(String organizationName) {
        if (!testsLabRepo.existsById(organizationName)) {
            throw new MessageError("No Test Lab With This Name");
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
    public String getAccess(String laboratoryName, String patientName) throws Exception {
        if (canAccessThisLab(laboratoryName) == 0) {
            throw new MessageError("User Can't Access This Lab");
        }
        if (patientRepo.existsById(patientName)) {
            Optional<TestsLabPatientEntity> testsLabPatientEntity = testsPatientRepo.
                    findByLaboratoryNameAndPatientName(laboratoryName, patientName);
            if (testsLabPatientEntity.isEmpty()) {
                TestsLabPatientEntity testsLabPatient = new TestsLabPatientEntity();
                testsLabPatient.setPatientName(patientName);
                testsLabPatient.setLaboratoryName(laboratoryName);
                testsLabPatient.setAccess(false);
                testsLabPatient.setWhichRequestAccess(false);
                testsPatientRepo.save(testsLabPatient);
                return "Request Access Done";
            }
            if (testsLabPatientEntity.get().getAccess()) {
                throw new MessageError("Already have access!");
            }
            throw new MessageError("Already request access");
        }
        throw new MessageError("No patient with this UserName");
    }

    @Override
    public String acceptAccess(String laboratoryName, String patientName) throws Exception {
        if (canAccessThisLab(laboratoryName) == 0) {
            throw new MessageError("User Can't Access This Laboratory");
        }
        Optional<TestsLabPatientEntity> testsLabPatient = testsPatientRepo.
                findByLaboratoryNameAndPatientName(laboratoryName, patientName);

        if (testsLabPatient.isPresent()) {
            if (!testsLabPatient.get().getAccess() && testsLabPatient.get().getWhichRequestAccess()) {
                testsLabPatient.get().setAccess(true);
                testsPatientRepo.save(testsLabPatient.get());
                return "Done Access Accept";
            } else {
                throw new MessageError("Already have access!");
            }
        }
        throw new MessageError("No Request to be accept");
    }

    @Override
    public String removeAccess(String laboratoryName, String patientName) throws Exception {
        if (canAccessThisLab(laboratoryName) == 0) {
            throw new MessageError("User Can't Access This Laboratory");
        }
        Optional<TestsLabPatientEntity> testsLabPatient = testsPatientRepo.
                findByLaboratoryNameAndPatientName(laboratoryName, patientName);
        if (testsLabPatient.isPresent()) {
            testsPatientRepo.deleteById(testsLabPatient.get().getTestsLaboratoryPatientsId());
            return "Done Access Removed";
        }
        throw new MessageError("Not Found!");
    }

    @Override
    public List<ConnectionsListDTO> getListOfConnections(String laboratoryName) {
        if (canAccessThisLab(laboratoryName) == 0) {
            throw new MessageError("User Can't Access This Laboratory");
        }
        List<TestsLabPatientEntity> testsLabPatientEntities =
                testsPatientRepo.findAllByLaboratoryName(laboratoryName);
        List<ConnectionsListDTO> connectionsListDTO = new ArrayList<>();
        for (TestsLabPatientEntity testsLabPatient :
                testsLabPatientEntities) {
            if (testsLabPatient.getAccess()) {
                connectionsListDTO.add(new ConnectionsListDTO(testsLabPatient.getPatientName()
                        , userRepo.findByUsername(
                        testsLabPatient.getPatientName()).get().getFullName()
                        , true, true));
            } else if (testsLabPatient.getWhichRequestAccess()) {
                connectionsListDTO.add(new ConnectionsListDTO(testsLabPatient.getPatientName()
                        , userRepo.findByUsername(
                        testsLabPatient.getPatientName()).get().getFullName()
                        , false, true));
            }
        }
        return connectionsListDTO;
    }


    @Override
    public List<PrescriptionDTOToViewAsList> getAllPrescriptionTOMyPatient(String laboratoryName
            , String patientName) throws Exception {
        if (canAccessThisLab(laboratoryName) == 0) {
            throw new MessageError("User Can't Access This Laboratory");
        }
        Optional<TestsLabPatientEntity> testsLabPatient = testsPatientRepo.
                findByLaboratoryNameAndPatientName(laboratoryName, patientName);
        if (testsLabPatient.isEmpty() || !testsLabPatient.get().getAccess()) {
            throw new MessageError("Laboratory don't have access to this patient");
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
    public List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByID(String laboratoryName
            , String patientName) throws Exception {
        return getAllPrescriptionTOMyPatient(laboratoryName, patientName);
    }

    @Override
    public List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByDoctorName(
            String laboratoryName, String patientName) throws Exception {
        List<PrescriptionDTOToViewAsList> result = getAllPrescriptionTOMyPatient(laboratoryName
                , patientName);
        result.sort((o1, o2) -> o1.getDoctorName().compareTo(o2.getDoctorName()) * -1);
        return result;
    }

    @Override
    public List<TestsInPrescriptionDTO> getPrescription(Long id, String laboratoryName) {
        if (canAccessThisLab(laboratoryName) == 0) {
            throw new MessageError("User Can't Access This laboratoryName");
        }
        List<TestsInPrescriptionDTO> testsInPrescriptionDTO = new ArrayList<>();
        Optional<PrescriptionEntity> prescription = prescriptionRepo.findById(id);
        if (prescription.isEmpty()) {
            throw new MessageError("No prescription with this ID");
        }
        Optional<TestsLabPatientEntity> testsLabPatient = testsPatientRepo.
                findByLaboratoryNameAndPatientName(laboratoryName, prescription.get().getPatientName());
        ;
        if (testsLabPatient.isEmpty() || !testsLabPatient.get().getAccess()) {
            throw new MessageError("laboratoryName didn't have access to this patient");
        }
        List<TestsPrescriptionEntity> tests = testsInPrescriptionRepo.
                findAllByPrescriptionId(id);
        for (TestsPrescriptionEntity testsPrescription : tests) {
            testsInPrescriptionDTO.add(new TestsInPrescriptionDTO(testsPrescription.getTest()
                    , testsPrescription.getNote()));
        }
        return testsInPrescriptionDTO;
    }

    @Override
    public String addTestResult(TestsResultDTO testsResultDTO, String laboratoryName) {
        if (testsPatientRepo.findByLaboratoryNameAndPatientName(
                laboratoryName, testsResultDTO.getPatientName()).isPresent()) {
            TestsEntity tests = testsMapper.toTestsEntity(testsResultDTO);
            testsRepo.save(tests);
            return "Add Test Result Successfully";
        }
        return "Laboratory Can't Access This Patient!";
    }
}
