package com.example.lastone.service;

import com.example.lastone.model.dto.AddOrganizationProfileDTO;
import com.example.lastone.model.dto.UserDTO;
import com.example.lastone.model.dto.UserRegisterDTO;
import com.example.lastone.model.entity.UserEntity;

import java.util.List;


public interface UserService {
    public boolean userRegister(UserRegisterDTO userRegisterDTO) throws Exception;

    public boolean addPatientProfile();

    public boolean addDoctorProfile(String specialization);

    public boolean addXRayLaboratoryProfile(AddOrganizationProfileDTO organizationProfileDTO);

    boolean havePatientProfile();

    UserDTO getListOfProfiles();

    boolean haveDoctorProfile();

    public List<UserEntity> get();
}
