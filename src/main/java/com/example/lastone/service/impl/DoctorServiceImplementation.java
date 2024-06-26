package com.example.lastone.service.impl;

import com.example.lastone.Exceptions.MessageError;
import com.example.lastone.model.dto.*;
import com.example.lastone.model.entity.*;
import com.example.lastone.model.mapper.PrescriptionMapperIm;
import com.example.lastone.model.mapper.TestsMapper;
import com.example.lastone.model.mapper.UserMapper;
import com.example.lastone.model.mapper.XRayMapperIm;
import com.example.lastone.repository.*;
import com.example.lastone.service.DoctorService;
import com.example.lastone.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@RequiredArgsConstructor
@Service
public class DoctorServiceImplementation implements DoctorService {
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
    private final XRayRepo xRayRepo;
    private final XRayMapperIm xRayMapper;
    private final TestsRepo testsRepo;
    private final TestsPatientRepo testsPatientRepo;
    private final TestsMapper testsMapper;

    @Override
    public String getUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @Override
    public List<PatientViewAsListDTO> getAllPatients() {
        List<DoctorPatientEntity> doctorPatientEntities = doctorPatientRepo.findAllByDoctorName(getUsername());
        List<PatientViewAsListDTO> result = new ArrayList<>();
        for (DoctorPatientEntity doctorPatient : doctorPatientEntities) {
            result.add(userMapper.toPatientViewToDoctorAsListDTO(userRepo.findById(doctorPatient.getPatientName()).get()));
        }
        return result;
    }

    @Override
    public PatientViewToDoctorDTO getPatient(String patientName) throws Exception {
        if (haveAccess(patientName)) {
            return userMapper.toPatientViewToDoctorDTO(userRepo.findByUsername(patientName).orElseThrow(NoSuchElementException::new));
        }
        throw new Exception("Doctor can't access this patient");
    }

    @Override
    public Boolean haveAccess(String patientName) {
        Optional<DoctorPatientEntity> entityDP = doctorPatientRepo.findByDoctorNameAndPatientName(getUsername(), patientName);
        if (entityDP.isPresent()) {
            return entityDP.get().getAccess();
        } else {
            throw new MessageError("Doctor can't access this patient");
        }
    }

    @Override
    public String acceptAccess(String patientName) throws Exception {
        Optional<DoctorPatientEntity> doctorPatient = doctorPatientRepo.findByDoctorNameAndPatientName(getUsername(), patientName);
        if (doctorPatient.isPresent()) {
            if (!doctorPatient.get().getAccess() && doctorPatient.get().getWhichRequestAccess() == 1) {
                doctorPatient.get().setAccess(true);
                doctorPatientRepo.save(doctorPatient.get());
                return "Accept Access Done";
            } else {
                throw new MessageError("Already have access!");
            }
        }
        throw new MessageError("No Request to be accept");
    }

    @Override
    public String removeAccess(String patientName) throws Exception {
        Optional<DoctorPatientEntity> doctorPatient = doctorPatientRepo.findByDoctorNameAndPatientName(getUsername(), patientName);
        if (doctorPatient.isPresent()) {
            doctorPatientRepo.deleteById(doctorPatient.get().getDoctorPatientId());
            return "Access Removed";
        }
        throw new MessageError("Not Found!");
    }

    @Override
    public String getAccess(String patientName) throws Exception {
        if (patientRepo.existsById(patientName)) {
            Optional<DoctorPatientEntity> doctorPatient = doctorPatientRepo.findByDoctorNameAndPatientName(getUsername(), patientName);
            if (!doctorPatient.isPresent()) {
                DoctorPatientEntity doctorPatientEntity = new DoctorPatientEntity();
                doctorPatientEntity.setPatientName(patientName);
                doctorPatientEntity.setDoctorName(getUsername());
                doctorPatientEntity.setAccess(false);
                doctorPatientEntity.setWhichRequestAccess(2);
                doctorPatientRepo.save(doctorPatientEntity);
                return "Request Access Done";
            }
            if (doctorPatient.get().getAccess()) {
                throw new MessageError("Already have access!");
            }
            throw new MessageError("Already request access!");
        }
        throw new MessageError("No patient with this UserName!");
    }

    @Override
    public List<ConnectionsListDTO> getListOfConnections() {
        List<DoctorPatientEntity> doctorPatientEntities = doctorPatientRepo.findAllByDoctorName(getUsername());
        List<ConnectionsListDTO> connectionsList = new ArrayList<>();
        for (DoctorPatientEntity doctorPatient : doctorPatientEntities) {
            if (doctorPatient.getAccess()) {
                connectionsList.add(new ConnectionsListDTO(doctorPatient.getPatientName(), userRepo.findById(doctorPatient.getPatientName()).get().getFullName(), doctorPatient.getAccess(), true));
            } else if (doctorPatient.getWhichRequestAccess() == 1) {
                connectionsList.add(new ConnectionsListDTO(doctorPatient.getPatientName(), userRepo.findById(doctorPatient.getPatientName()).get().getFullName(), doctorPatient.getAccess(), true));
            }
        }
        return connectionsList;
    }

    @Override
    public PatientViewToDoctorDTO findPatient(String username) throws IOException {
        Optional<PatientEntity> patientEntity = patientRepo.findById(username);
        if (patientEntity.isEmpty()) {
            throw new NoSuchElementException("Patient Not Found!");
        }
        PatientViewToDoctorDTO patient = userMapper.toPatientViewToDoctorDTO(userRepo.findById(username).get());
        patient.setAge(Period.between(patient.getDateOfBirth(), LocalDate.now()).getYears());
        return patient;
    }

    @Override
    public PrescriptionViewDTO getPrescriptionTOMyPatient(Long ID, String patientName) throws Exception {
        Optional<DoctorPatientEntity> doctorPatient = doctorPatientRepo.
                findByDoctorNameAndPatientName(getUsername(),
                        patientName);
        if (doctorPatient.isEmpty() || !doctorPatient.get().getAccess()) {
            throw new MessageError("Doctor can't access this patient");
        }
        Optional<PrescriptionEntity> prescription = prescriptionRepo.findById(ID);
        if (prescription.isPresent()) {
            if (prescription.get().getPatientName().equals(patientName)) {
                return prescriptionMapper.toPrescriptionViewDTO(prescription.get());
            }
            throw new MessageError("Patient didn't have prescription with this ID");
        }
        throw new Exception("No prescription with this id");
    }

    @Override
    public List<PrescriptionDTOToViewAsList> getAllPrescriptionTOMyPatient(String patientName) throws Exception {
        Optional<DoctorPatientEntity> doctorPatient = doctorPatientRepo.
                findByDoctorNameAndPatientName(getUsername(),
                        patientName);
        if (doctorPatient.isEmpty() || !doctorPatient.get().getAccess()) {
            throw new MessageError("Doctor can't access this patient");
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
    public List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByID(String patientName) throws Exception {
        return getAllPrescriptionTOMyPatient(patientName);
    }

    @Override
    public List<PrescriptionDTOToViewAsList> getAllPrescriptionSortedByDoctorName(String patientName) throws Exception {
        List<PrescriptionDTOToViewAsList> result = getAllPrescriptionTOMyPatient(patientName);
        result.sort(new Comparator<PrescriptionDTOToViewAsList>() {
            @Override
            public int compare(PrescriptionDTOToViewAsList o1, PrescriptionDTOToViewAsList o2) {
                return o1.getDoctorName().compareTo(o2.getDoctorName()) * -1;
            }
        });
        return result;
    }

    @Override
    public PrescriptionViewDTO addPrescriptionToMyPatient(PrescriptionAddFromDoctorDTO prescriptionEntity) throws Exception {
        if (!doctorPatientRepo.existsByDoctorNameAndPatientName(getUsername(), prescriptionEntity.getPatientName()) || !doctorPatientRepo.findByDoctorNameAndPatientName(getUsername(), prescriptionEntity.getPatientName()).get().getAccess()) {

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
        for (XRayInPrescriptionDTO xRayInPrescriptionDTO : prescriptionEntity.getXr()) {
            XRayInPrescriptionEntity xRayInPrescription = new XRayInPrescriptionEntity();
            xRayInPrescription.setPrescriptionId(prescription.getId());
            xRayInPrescription.setXRay(xRayInPrescriptionDTO.getXray());
            xRayInPrescription.setNote(xRayInPrescriptionDTO.getNote());
            xRayInPrescriptionRepo.save(xRayInPrescription);
        }
        for (TestsInPrescriptionDTO tests : prescriptionEntity.getTests()) {
            TestsPrescriptionEntity testsEntity = new TestsPrescriptionEntity();
            testsEntity.setPrescriptionId(prescription.getId());
            testsEntity.setTest(tests.getTest());
            testsEntity.setNote(tests.getNote());
            testsInPrescriptionRepo.save(testsEntity);
        }
        return new PrescriptionViewDTO(prescription.getId(), prescription.getPatientName(), prescription.getDoctorName(), prescriptionEntity.getMedicines(), prescriptionEntity.getXr(), prescriptionEntity.getTests(), prescriptionEntity.getNote(), prescriptionEntity.getDiagnosis(), prescription.getCreatedAt());
    }

    @Override
    public List<ResponseAsListDTO> getListOfXRay(String patientName) {
        if (!haveAccess(patientName)) {
            throw new MessageError("Doctor can't access this patient");
        }
        List<XRayEntity> xRayEntityList = xRayRepo.findAllByPatientName(patientName);
        List<ResponseAsListDTO> responseAsListDTOList = new ArrayList<>();
        for (XRayEntity xRay : xRayEntityList) {
            responseAsListDTOList.add(xRayMapper.toXRayAsListDto(xRay));
        }
        responseAsListDTOList.sort((o1, o2) -> {
            int c = o1.getCategory().compareTo(o2.getCategory());
            if (c > 0) {
                return 1;
            } else if (c < 0) {
                return -1;
            } else {
                return o1.getId().compareTo(o2.getId()) * -1;
            }
        });
        return responseAsListDTOList;
    }

    @Override
    public byte[] getXRayPicture(Long id, String patientName) {
        if (!haveAccess(patientName)) {
            throw new MessageError("Doctor can't access this patient");
        }
        Optional<XRayEntity> xRay = xRayRepo.findById(id);
        if (xRay.isPresent()) {
            if (!xRay.get().getPatientName().equals(patientName)) {
                throw new MessageError("User can't view this picture");
            }
            byte image[] = ImageUtils.decompressImage(xRay.get().getPicture());
            return image;
        } else {
            throw new MessageError("NO XRay Picture With This ID");
        }
    }

    @Override
    public List<ResponseAsListDTO> getListOfTests(String patientName) {
        if (!haveAccess(patientName)) {
            throw new MessageError("Doctor can't access this patient");
        }
        List<TestsEntity> testsEntities = testsRepo.findAllByPatientName(patientName);
        List<ResponseAsListDTO> responseAsListDTOList = new ArrayList<>();
        for (TestsEntity tests : testsEntities) {
            responseAsListDTOList.add(xRayMapper.toXRayAsListDto(tests));
        }
        responseAsListDTOList.sort((o1, o2) -> {
            int c = o1.getCategory().compareTo(o2.getCategory());
            if (c > 0) {
                return 1;
            } else if (c < 0) {
                return -1;
            } else {
                return o1.getId().compareTo(o2.getId()) * -1;
            }
        });
        return responseAsListDTOList;
    }

    @Override
    public TestsResultDTO getTestDetails(Long ID, String patientName) {
        if (!haveAccess(patientName)) {
            throw new MessageError("Doctor can't access this patient");
        }
        Optional<TestsEntity> test = testsRepo.findById(ID);
        if (test.isPresent()) {
            if (!test.get().getPatientName().equals(patientName)) {
                throw new MessageError("The patient didn't have test with this ID");
            }
            return testsMapper.toTestsResultDto(test.get());
        } else {
            throw new MessageError("NO Test With This ID");
        }
    }
}
