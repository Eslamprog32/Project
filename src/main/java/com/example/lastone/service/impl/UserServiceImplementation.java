package com.example.lastone.service.impl;

import com.example.lastone.Exceptions.MessageError;
import com.example.lastone.model.dto.*;
import com.example.lastone.model.entity.*;
import com.example.lastone.model.mapper.OrganizationMapper;
import com.example.lastone.model.mapper.UserMapper;
import com.example.lastone.model.mapper.UserMapperIm;
import com.example.lastone.repository.*;
import com.example.lastone.service.UserService;
import com.example.lastone.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImplementation implements UserService {
    private final UserRepo userRepo;
    private final DoctorRepo doctorRepo;
    private final XRayLaboratoryRepo xRayLaboratoryRepo;
    private final OrganizationRepo organizationRepo;
    private final PatientRepo patientRepo;
    private final UserMapper userMapper;
    private final UserMapperIm userMapperIm;
    private final OrganizationMapper organizationMapper;
    private final WorksInRepo worksInRepo;
    private final PasswordEncoder passwordEncoder;
    private final PharmacistRepo pharmacistRepo;
    private final TestsLabRepo testsLabRepo;


    private String getUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @Override
    public boolean havePatientProfile() {
        return patientRepo.existsById(getUsername());
    }

    @Override
    public UserDTO getListOfProfiles() {
        UserDTO userDTO = new UserDTO();
        userDTO.setPatient(havePatientProfile());
        userDTO.setDoctor(haveDoctorProfile());
        userDTO.setOrganizationDTOList(new ArrayList<>());
        List<WorksInEntity> organizations = worksInRepo.findAllByUsername(getUsername());
        for (WorksInEntity organization : organizations) {
            userDTO.getOrganizationDTOList().add(new ViewOrganizationDTO(organization.
                    getOrganizationName(), organization.getType(), organization.getAdmin()));
        }
        return userDTO;
    }

    @Override
    public boolean haveDoctorProfile() {
        return doctorRepo.existsById(getUsername());
    }

    @Override
    public List<UserEntity> get() {
        return userRepo.findAll();
    }

    @Override
    public GeneralInfoOfUserDTO getGeneralInfoOfUser() {
        UserEntity user = userRepo.findByUsername(getUsername()).get();
        return userMapperIm.toGeneralInfoOfUserDto(user);
    }

    @Override
    public String addPatientProfile() {
        Optional<UserEntity> userEntity = userRepo.findByUsername(getUsername());
        UserEntity user = userEntity.get();
        if (user.getPatientEntity() == null) {
            String roles = user.getRole();
            if (!roles.isEmpty()) {
                roles += ", ";
            }
            roles += "PATIENT";
            user.setRole(roles);
            PatientEntity patientEntity = new PatientEntity(userEntity.get().getUsername());
            patientRepo.save(patientEntity);
            userRepo.save(user);
            return "Add Patient Profile Successfully!";
        }
        throw new MessageError("User Already Have Patient Profile!");
    }

    @Override
    public String addDoctorProfile(DoctorDto dto) {
        Optional<UserEntity> userEntity = userRepo.findByUsername(getUsername());
        if (userEntity.isPresent()) {
            UserEntity user = userEntity.get();
            if (user.getDoctor() == null) {
                String role = user.getRole();
                if (!role.isEmpty()) {
                    role += ", ";
                }
                role += "DOCTOR";
                userEntity.get().setRole(role);
                DoctorEntity doctor = new DoctorEntity();
                doctor.setDoctorName(user.getUsername());
                doctor.setSpecialization(dto.getSpecialization());
                doctorRepo.save(doctor);
                userRepo.save(user);
                return "Add Doctor Profile Successfully!";
            }
        }
        throw new MessageError("User Already Have Doctor Profile!");
    }

    @Override
    public String addXRayLaboratoryProfile(OrganizationProfileDTO organizationProfileDTO) {
        OrganizationEntity organization = organizationMapper.toOrganizationEntity(organizationProfileDTO);
        if (organizationRepo.existsById(organization.getOrganizationName())) {
            throw new MessageError("username already used");
        }
        organization.setType("xray_lab");
        organizationRepo.save(organization);
        XRayLaboratoryEntity xRayLaboratory = new XRayLaboratoryEntity();
        xRayLaboratory.setXRayLaboratoryName(organization.getOrganizationName());
        xRayLaboratoryRepo.save(xRayLaboratory);
        WorksInEntity works = new WorksInEntity();
        works.setUsername(getUsername());
        works.setType("xray_lab");
        works.setOrganizationName(organization.getOrganizationName());
        works.setAdmin(true);
        worksInRepo.save(works);
        return "Add XRay-Lab Profile Successfully!";
    }

    @Override
    public String addPharmacyProfile(OrganizationProfileDTO organizationProfileDTO) {
        OrganizationEntity organization =
                organizationMapper.toOrganizationEntity(organizationProfileDTO);

        if (organizationRepo.existsById(organization.getOrganizationName())) {
            throw new MessageError("username already used");
        }
        organization.setType("pharmacy");
        organizationRepo.save(organization);
        PharmacistEntity pharmacistEntity = new PharmacistEntity();
        pharmacistEntity.setPharmacistName(organization.getOrganizationName());
        pharmacistRepo.save(pharmacistEntity);
        WorksInEntity works = new WorksInEntity();
        works.setUsername(getUsername());
        works.setType("pharmacy");
        works.setOrganizationName(organization.getOrganizationName());
        works.setAdmin(true);
        worksInRepo.save(works);
        return "Add Pharmacy Profile Successfully!";
    }

    @Override
    public String addTestLabProfile(OrganizationProfileDTO organizationProfileDTO) {
        OrganizationEntity organization = organizationMapper.toOrganizationEntity(organizationProfileDTO);
        if (organizationRepo.existsById(organization.getOrganizationName())) {
            throw new MessageError("username already used");
        }
        organization.setType("test_lab");
        organizationRepo.save(organization);
        TestsLabEntity testsLab = new TestsLabEntity();
        testsLab.setTestsLaboratoryName(organization.getOrganizationName());
        testsLabRepo.save(testsLab);
        WorksInEntity works = new WorksInEntity();
        works.setUsername(getUsername());
        works.setType("test_lab");
        works.setOrganizationName(organization.getOrganizationName());
        works.setAdmin(true);
        worksInRepo.save(works);
        return "Add Test-Lab Profile Successfully!";
    }

    @Override
    public String addProfilePicture(MultipartFile file) throws IOException {
        UserEntity user = userRepo.findByUsername(getUsername()).get();
        user.setProfilePic(ImageUtils.compressImage(file.getBytes()));
        user = userRepo.save(user);
        if (user.getProfilePic() != null) {
            return "add image successfully";
        }
        throw new MessageError("something wrong !");
    }

    @Override
    public byte[] getProfilePicture() throws IOException {
        UserEntity user = userRepo.findByUsername(getUsername()).get();
        if (user.getProfilePic() == null) {
            throw new MessageError("No ProfilePicture!");
        }
        byte[] images = ImageUtils.decompressImage(user.getProfilePic());
        return images;
    }

    @Override
    public byte[] getPic(String username) throws IOException {
        UserEntity user = userRepo.findByUsername(username).get();
        byte[] images = ImageUtils.decompressImage(user.getProfilePic());
        return images;
    }

    @Override
    public ResponseEntity<?> modifyUserData(UserModifyDTO userModifyDTO) {
        UserEntity user = userRepo.findByUsername(getUsername()).get();

        if (userModifyDTO.getOldPassword() != null) {
            if (!passwordEncoder.matches(userModifyDTO.getOldPassword(), user.getPassword())) {
                return ResponseEntity.status(403).body("Password incorrect!");
            }
            if (userModifyDTO.getNewPassword() == null) {
                return ResponseEntity.status(403).body("You can't make new password empty!");
            }
            user.setPassword(passwordEncoder.encode(userModifyDTO.getNewPassword()));
        }
        if (userModifyDTO.getFullName() != null) {
            user.setFullName(userModifyDTO.getFullName());
        }
        if (userModifyDTO.getAddress() != null) {
            user.setAddress(userModifyDTO.getAddress());
        }
        if (userModifyDTO.getGender() != null) {
            user.setGender(userModifyDTO.getGender());
        }
        if (userModifyDTO.getMartalStatus() != null) {
            user.setMartalStatus(userModifyDTO.getMartalStatus());
        }
        if (userModifyDTO.getPhone() != null) {
            user.setPhone(userModifyDTO.getPhone());
        }
        if (userModifyDTO.getDateOfBirth() != null) {
            user.setDateOfBirth(userModifyDTO.getDateOfBirth());
        }
        userRepo.save(user);
        return ResponseEntity.status(200).body("ok");
    }

    public UserEntity findUserByEmail(String email) {
        return userRepo.findByEmail(email).get();
    }



}
