package com.example.lastone.service;

import com.example.lastone.model.dto.*;
import com.example.lastone.model.entity.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface UserService {

    GeneralInfoOfUserDTO getGeneralInfoOfUser();

    String addPatientProfile();

    ResponseEntity<?> modifyUserData(UserModifyDTO userModifyDTO);


    String addDoctorProfile(DoctorDto dto);

    String addXRayLaboratoryProfile(OrganizationProfileDTO organizationProfileDTO);

    boolean havePatientProfile();

    UserDTO getListOfProfiles();

    boolean haveDoctorProfile();

    List<UserEntity> get();

    String addPharmacyProfile(OrganizationProfileDTO organizationProfileDTO);

    String addTestLabProfile(OrganizationProfileDTO organizationProfileDTO);

    String addProfilePicture(MultipartFile file) throws IOException;

    byte[] getProfilePicture() throws IOException;

    byte[] getPic(String username) throws IOException;
}
