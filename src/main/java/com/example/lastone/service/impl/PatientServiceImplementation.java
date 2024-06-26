package com.example.lastone.service.impl;

import com.example.lastone.Exceptions.MessageError;
import com.example.lastone.model.dto.*;
import com.example.lastone.model.entity.*;
import com.example.lastone.model.mapper.PrescriptionMapperIm;
import com.example.lastone.model.mapper.TestsMapper;
import com.example.lastone.model.mapper.UserMapper;
import com.example.lastone.model.mapper.XRayMapperIm;
import com.example.lastone.repository.*;
import com.example.lastone.service.PatientService;
import com.example.lastone.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    private final XRayRepo xRayRepo;
    private final XRayMapperIm xRayMapper;
    private final PharmacistPatientRepo pharmacistPatientRepo;
    private final TestsRepo testsRepo;
    private final TestsPatientRepo testsPatientRepo;
    private final TestsMapper testsMapper;

    public String getUserName() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @Override
    public List<PrescriptionDTOToViewAsList> getAllPrescriptionsV2(String username) {
        List<PrescriptionDTOToViewAsList> result = new ArrayList<>();
        List<PrescriptionEntity> prescriptionEntities = prescriptionRepo.findAllByPatientName(username);
        for (PrescriptionEntity prescription : prescriptionEntities) {
            result.add(prescriptionMapper.toPrescriptionDTOToViewAsList(prescription));
        }
        return result;
    }

    @Override
    public PrescriptionViewDTO getPrescriptionsById(Long id) throws Exception {
        Optional<PrescriptionEntity> prescriptionEntities = prescriptionRepo.findById(id);
        if (prescriptionEntities.isPresent()) {
            if (!prescriptionEntities.get().getPatientName().equals(getUserName())) {
                throw new MessageError(("User can't access this prescription!"));
            }
            return prescriptionMapper.toPrescriptionViewDTO(prescriptionEntities.get());
        } else {
            throw new MessageError(("Prescription Not Found!"));
        }
    }

    @Override
    public boolean removePrescriptionById(Long id) {
        if (prescriptionRepo.existsByPatientNameAndId(getUserName(), id)) {
            prescriptionRepo.deleteByPatientNameAndId(getUserName(), id);
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
    public String acceptDoctorAccess(String doctorName) throws Exception {
        Optional<DoctorPatientEntity> doctorPatient = doctorPatientRepo.findByDoctorNameAndPatientName(doctorName, getUserName());
        if (doctorPatient.isPresent()) {
            if (!doctorPatient.get().getAccess()) {
                doctorPatient.get().setAccess(true);
                doctorPatientRepo.save(doctorPatient.get());
                return "Done Accept Access";
            } else {
                throw new Exception("Already have access!");
            }
        }
        throw new Exception("No Request to be accept");
    }

    @Override
    public String removeDoctorAccess(String doctorName) throws Exception {
        Optional<DoctorPatientEntity> doctorPatient = doctorPatientRepo.findByDoctorNameAndPatientName(doctorName, getUserName());
        if (doctorPatient.isPresent()) {
            doctorPatientRepo.deleteById(doctorPatient.get().getDoctorPatientId());
            return "Access Removed";
        }
        throw new Exception("Not Found!");
    }

    @Override
    public String giveAccessToDoctor(String doctorName) throws Exception {
        if (doctorRepo.existsByDoctorName(doctorName)) {
            Optional<DoctorPatientEntity> doctorPatient = doctorPatientRepo.findByDoctorNameAndPatientName(doctorName, getUserName());
            if (doctorPatient.isEmpty()) {
                DoctorPatientEntity doctorPatientEntity = new DoctorPatientEntity();
                doctorPatientEntity.setPatientName(getUserName());
                doctorPatientEntity.setDoctorName(doctorName);
                doctorPatientEntity.setAccess(false);
                doctorPatientEntity.setWhichRequestAccess(1);
                doctorPatientRepo.save(doctorPatientEntity);
                return "Request Access Done";
            }
            if (doctorPatient.get().getAccess()) {
                throw new MessageError("Already have access!");
            }
            throw new MessageError("Already request access");
        }
        throw new MessageError("No doctor with this UserName");
    }

    @Override
    public List<ConnectionsListDTO> getListOfConnections() {
        List<DoctorPatientEntity> doctorPatientEntities = doctorPatientRepo.findAllByPatientName(getUserName());
        List<ConnectionsListDTO> connectionsList = new ArrayList<>();
        for (DoctorPatientEntity doctorPatient : doctorPatientEntities) {
            if (doctorPatient.getAccess()) {
                connectionsList.add(new ConnectionsListDTO(doctorPatient.getDoctorName()
                        , userRepo.findById(doctorPatient.getDoctorName()).get().getFullName()
                        , doctorPatient.getAccess(), true));
            } else if (doctorPatient.getWhichRequestAccess() == 2) {
                connectionsList.add(new ConnectionsListDTO(doctorPatient.getDoctorName()
                        , userRepo.findById(doctorPatient.getDoctorName()).get().getFullName()
                        , doctorPatient.getAccess(), true));
            }
        }
        List<XRayLaboratoryPatientEntity> xRayLaboratoryPatientEntityList =
                xRayLaboratoryPatientRepo.findAllByPatientName(getUserName());
        for (XRayLaboratoryPatientEntity xRayLaboratoryPatient :
                xRayLaboratoryPatientEntityList) {
            if (xRayLaboratoryPatient.getAccess()) {
                connectionsList.add(new ConnectionsListDTO(xRayLaboratoryPatient.getLaboratoryName()
                        , "xray_lab", true, false));
            } else if (!xRayLaboratoryPatient.getWhichRequestAccess()) {
                connectionsList.add(new ConnectionsListDTO(
                        xRayLaboratoryPatient.getLaboratoryName()
                        , "xray_lab", true, false));
            }
        }
        List<PharmacistPatientEntity> pharmacistPatientEntities =
                pharmacistPatientRepo.findAllByPatientName(getUserName());
        for (PharmacistPatientEntity pharmacistPatientEntity :
                pharmacistPatientEntities) {
            if (pharmacistPatientEntity.getAccess()) {
                connectionsList.add(new ConnectionsListDTO(pharmacistPatientEntity.getPharmacistName()
                        , "pharmacy", true, false));
            } else if (!pharmacistPatientEntity.getWhichRequestAccess()) {
                connectionsList.add(new ConnectionsListDTO(
                        pharmacistPatientEntity.getPharmacistName()
                        , "pharmacy", true, false));
            }
        }
        List<TestsLabPatientEntity> testsLabPatientEntities =
                testsPatientRepo.findAllByPatientName(getUserName());
        for (TestsLabPatientEntity testsLabPatient :
                testsLabPatientEntities) {
            if (testsLabPatient.getAccess()) {
                connectionsList.add(new ConnectionsListDTO(testsLabPatient.getLaboratoryName()
                        , "test_lab", true, false));
            } else if (!testsLabPatient.getWhichRequestAccess()) {
                connectionsList.add(new ConnectionsListDTO(
                        testsLabPatient.getLaboratoryName()
                        , "test_lab", true, false));
            }
        }
        return connectionsList;
    }

    @Override
    public String giveAccessPharmacy(String pharmacistName) throws Exception {
        if (xRayLaboratoryRepo.existsById(pharmacistName)) {
            Optional<PharmacistPatientEntity> pharmacistPatient = pharmacistPatientRepo.
                    findByPharmacistNameAndPatientName(pharmacistName, getUserName());
            if (pharmacistPatient.isEmpty()) {
                PharmacistPatientEntity pharmacistPatientEntity = new PharmacistPatientEntity();
                pharmacistPatientEntity.setPatientName(getUserName());
                pharmacistPatientEntity.setPharmacistName(pharmacistName);
                pharmacistPatientEntity.setAccess(false);
                pharmacistPatientEntity.setWhichRequestAccess(true);
                pharmacistPatientRepo.save(pharmacistPatientEntity);
                return "Request Access Done";
            }
            if (pharmacistPatient.get().getAccess()) {
                throw new MessageError("Already have access!");
            }
            throw new MessageError("Already request access");
        }
        throw new MessageError("No Pharmacy with this UserName");
    }

    @Override
    public String acceptPharmacyAccess(String pharmacistName) throws Exception {
        Optional<PharmacistPatientEntity> pharmacistPatient = pharmacistPatientRepo.
                findByPharmacistNameAndPatientName(pharmacistName, getUserName());
        if (pharmacistPatient.isPresent()) {
            if (!pharmacistPatient.get().getAccess()
                    && !pharmacistPatient.get().getWhichRequestAccess()) {
                pharmacistPatient.get().setAccess(true);
                pharmacistPatientRepo.save(pharmacistPatient.get());
                return "Done Accept Access";
            } else {
                throw new MessageError("Already have access!");
            }
        }
        throw new MessageError("No Request to be accept");
    }

    @Override
    public String removePharmacyAccess(String pharmacistName) throws Exception {
        Optional<PharmacistPatientEntity> pharmacistPatient = pharmacistPatientRepo.
                findByPharmacistNameAndPatientName(pharmacistName, getUserName());
        if (pharmacistPatient.isPresent()) {
            xRayLaboratoryPatientRepo.deleteById(pharmacistPatient.get().getPharmacistPatientID());
            return "Access Removed";
        }
        throw new MessageError("Not Found!");
    }

    @Override
    public String giveAccessXRayLaboratory(String xRayLaboratoryName) throws Exception {
        if (xRayLaboratoryRepo.existsById(xRayLaboratoryName)) {
            Optional<XRayLaboratoryPatientEntity> xRayLaboratoryPatientEntity = xRayLaboratoryPatientRepo.findByLaboratoryNameAndPatientName(xRayLaboratoryName, getUserName());
            if (xRayLaboratoryPatientEntity.isEmpty()) {
                XRayLaboratoryPatientEntity xRayLaboratoryPatient = new XRayLaboratoryPatientEntity();
                xRayLaboratoryPatient.setPatientName(getUserName());
                xRayLaboratoryPatient.setLaboratoryName(xRayLaboratoryName);
                xRayLaboratoryPatient.setAccess(false);
                xRayLaboratoryPatient.setWhichRequestAccess(true);
                xRayLaboratoryPatientRepo.save(xRayLaboratoryPatient);
                return "Request Access Done";
            }
            if (xRayLaboratoryPatientEntity.get().getAccess()) {
                throw new MessageError("Already have access!");
            }
            throw new MessageError("Already request access");
        }
        throw new MessageError("No xRayLaboratory with this UserName");
    }

    @Override
    public String acceptXRayLaboratoryAccess(String xRayLaboratoryName) throws Exception {
        Optional<XRayLaboratoryPatientEntity> xRayLaboratoryPatientEntity = xRayLaboratoryPatientRepo.findByLaboratoryNameAndPatientName(xRayLaboratoryName, getUserName());
        if (xRayLaboratoryPatientEntity.isPresent()) {
            if (!xRayLaboratoryPatientEntity.get().getAccess() && !xRayLaboratoryPatientEntity.get().getWhichRequestAccess()) {
                xRayLaboratoryPatientEntity.get().setAccess(true);
                xRayLaboratoryPatientRepo.save(xRayLaboratoryPatientEntity.get());
                return "Done Accept Access";
            } else {
                throw new MessageError("Already have access!");
            }
        }
        throw new MessageError("No Request to be accept");
    }

    @Override
    public String removeXRayLaboratoryAccess(String xRayLaboratoryName) throws Exception {
        Optional<XRayLaboratoryPatientEntity> xRayLaboratoryPatientEntity = xRayLaboratoryPatientRepo.findByLaboratoryNameAndPatientName(xRayLaboratoryName, getUserName());
        if (xRayLaboratoryPatientEntity.isPresent()) {
            xRayLaboratoryPatientRepo.deleteById(xRayLaboratoryPatientEntity.get().getXRaysLaboratoryPatientsId());
            return "Access Removed";
        }
        throw new MessageError("Not Found!");
    }

    @Override
    public String giveAccessTestsLaboratory(String laboratoryName) throws Exception {
        if (xRayLaboratoryRepo.existsById(laboratoryName)) {
            Optional<TestsLabPatientEntity> testsLabPatientEntity = testsPatientRepo.
                    findByLaboratoryNameAndPatientName(laboratoryName, getUserName());
            if (testsLabPatientEntity.isEmpty()) {
                TestsLabPatientEntity testsLabPatient = new TestsLabPatientEntity();
                testsLabPatient.setPatientName(getUserName());
                testsLabPatient.setLaboratoryName(laboratoryName);
                testsLabPatient.setAccess(false);
                testsLabPatient.setWhichRequestAccess(true);
                testsPatientRepo.save(testsLabPatient);
                return "Request Access Done";
            }
            if (testsLabPatientEntity.get().getAccess()) {
                throw new MessageError("Already have access!");
            }
            throw new MessageError("Already request access");
        }
        throw new MessageError("No xRayLaboratory with this UserName");
    }


    @Override
    public String acceptTestsLabAccess(String laboratoryName) throws Exception {
        Optional<TestsLabPatientEntity> testsLabPatient = testsPatientRepo.
                findByLaboratoryNameAndPatientName(laboratoryName, getUserName());
        if (testsLabPatient.isPresent()) {
            if (!testsLabPatient.get().getAccess() && !testsLabPatient.get().getWhichRequestAccess()) {
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
    public String removeTestsLabAccess(String laboratoryName) throws Exception {
        Optional<TestsLabPatientEntity> testsLabPatient = testsPatientRepo.
                findByLaboratoryNameAndPatientName(laboratoryName, getUserName());
        if (testsLabPatient.isPresent()) {
            testsPatientRepo.deleteById(testsLabPatient.get().getTestsLaboratoryPatientsId());
            return "Done Access Removed";
        }
        throw new MessageError("Not Found!");
    }

    @Override
    public List<ResponseAsListDTO> getListOfXRay() {
        List<XRayEntity> xRayEntityList = xRayRepo.findAllByPatientName(getUserName());
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
    public List<ResponseAsListDTO> getListOfTests() {
        List<TestsEntity> testsEntities = testsRepo.findAllByPatientName(getUserName());
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
    public TestsResultDTO getTestDetails(Long ID) {
        Optional<TestsEntity> test = testsRepo.findById(ID);
        if (test.isPresent()) {
            return testsMapper.toTestsResultDto(test.get());
        } else {
            throw new MessageError("NO Test With This ID");
        }
    }

    @Override
    public byte[] getXRayPicture(Long id) {
        XRayEntity xRay = xRayRepo.findById(id).get();
        if (!xRay.getPatientName().equals(getUserName())) {
            throw new MessageError("User can't view this picture");
        }
        if (xRay.getPicture() == null) {
            throw new MessageError("No XRay Picture With This ID!");
        }
        byte image[] = ImageUtils.decompressImage(xRay.getPicture());
        return image;
    }
}
