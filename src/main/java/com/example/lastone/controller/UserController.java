package com.example.lastone.controller;

import com.example.lastone.model.dto.AddOrganizationProfileDTO;
import com.example.lastone.model.dto.UserDTO;
import com.example.lastone.model.dto.UserRegisterDTO;
import com.example.lastone.model.entity.DoctorEntity;
import com.example.lastone.model.entity.PatientEntity;
import com.example.lastone.model.entity.UserEntity;
import com.example.lastone.service.UserService;
import com.example.lastone.service.impl.UserServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/Register")
    public boolean userRegister(@RequestBody UserRegisterDTO userRegisterDTO) throws Exception {
        return userService.userRegister(userRegisterDTO);
    }


    @GetMapping("/get")
    public List<UserEntity> get() {
        return userService.get();
    }

    @GetMapping("/username")
    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
    @GetMapping("/auth/get-list-of-profiles")
    public UserDTO getListOfProfiles(){
        return userService.getListOfProfiles();
    }

    @GetMapping("/auth/have-patient-profile")
    public boolean havePatientProfile() {
        return userService.havePatientProfile();
    }

    @GetMapping("/auth/have-doctor-profile")
    public boolean haveDoctorProfile() {
        return userService.haveDoctorProfile();
    }

    @PostMapping("/auth/add-patient-profile")
    public boolean addPatientProfile() {
        return userService.addPatientProfile();
    }

    @PostMapping("/auth/add-doctor-profile")
    public boolean addDoctorProfile(@RequestParam String specialization) {
        return userService.addDoctorProfile(specialization);
    }

    @PostMapping("/auth/add-xray-laboratory-profile")
    public boolean addXRayLaboratoryProfile(@RequestBody AddOrganizationProfileDTO organizationProfileDTO) {
        return userService.addXRayLaboratoryProfile(organizationProfileDTO);
    }
}
