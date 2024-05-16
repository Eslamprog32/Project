package com.example.lastone.service.impl;

import com.example.lastone.model.dto.AddOrganizationProfileDTO;
import com.example.lastone.model.dto.UserRegisterDTO;
import com.example.lastone.model.entity.*;
import com.example.lastone.model.mapper.OrganizationMapper;
import com.example.lastone.model.mapper.UserMapper;
import com.example.lastone.repository.*;
import com.example.lastone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
    private final OrganizationMapper organizationMapper;
    private final WorksInRepo worksInRepo;


    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Override
    public List<UserEntity> get() {
        return userRepo.findAll();
    }

    @Override
    public boolean userRegister(UserRegisterDTO userRegisterDTO) throws Exception {
        Optional<UserEntity> entityByUserName = userRepo.findByUsername(userRegisterDTO.getUsername());
        Optional<UserEntity> entityByEmail = userRepo.findByEmail(userRegisterDTO.getEmail());
        Optional<UserEntity> entityByPhone = userRepo.findByPhone(userRegisterDTO.getPhone());
        Optional<UserEntity> entityBySSN = userRepo.findBySSN(userRegisterDTO.getSSN());
        if (entityByUserName.isPresent()) {
            throw new Exception("UserName already used!");
        }
        if (entityBySSN.isPresent()) {
            throw new Exception("SSN already used!");
        }
        if (entityByEmail.isPresent()) {
            throw new Exception("Email already used!");
        }
        if (entityByPhone.isPresent()) {
            throw new Exception("Phone already used!");
        }
        UserEntity userEntity = userMapper.toEntity(userRegisterDTO);
        userEntity.setRole("");
        userRepo.save(userEntity);
        return true;
    }

    @Override
    public boolean addPatientProfile() {
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
            return true;
        }
        return false;
    }

    @Override
    public boolean addDoctorProfile(String specialization) {
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
                doctor.setSpecialization(specialization);
                doctorRepo.save(doctor);
                userRepo.save(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addXRayLaboratoryProfile(AddOrganizationProfileDTO organizationProfileDTO) {
        OrganizationEntity organization = organizationMapper.toOrganizationEntity(organizationProfileDTO);
        if (organizationRepo.existsById(organization.getOrganizationName())) {
            return false;
        }
        organizationRepo.save(organization);
        XRayLaboratoryEntity xRayLaboratory = new XRayLaboratoryEntity();
        xRayLaboratory.setXRayLaboratoryName(organization.getOrganizationName());
        xRayLaboratoryRepo.save(xRayLaboratory);
        WorksInEntity works = new WorksInEntity();
        works.setUsername(getUsername());
        works.setOrganizationName(organization.getOrganizationName());
        worksInRepo.save(works);
        return true;
    }

}
