package com.example.lastone.controller;

import com.example.lastone.model.dto.*;
import com.example.lastone.model.entity.UserEntity;
import com.example.lastone.service.ImageService;
import com.example.lastone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users/auth")
//@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;
    private final ImageService imageService;

    @GetMapping("/get")
    public List<UserEntity> get() {
        return userService.get();
    }

    @GetMapping("/get-list-of-profiles")
    public UserDTO getListOfProfiles() {
        return userService.getListOfProfiles();
    }

    @GetMapping("/have-patient-profile")
    public boolean havePatientProfile() {
        return userService.havePatientProfile();
    }

    @GetMapping("/have-doctor-profile")
    public boolean haveDoctorProfile() {
        return userService.haveDoctorProfile();
    }

    @PostMapping("/add-patient-profile")
    public ResponseEntity<String> addPatientProfile() {
        return ResponseEntity.ok(userService.addPatientProfile());
    }

    @PostMapping("/add-doctor-profile")
    public String addDoctorProfile(@RequestBody DoctorDto dto) {
        return userService.addDoctorProfile(dto);
    }

    @PostMapping("/add-xray-laboratory-profile")
    public String addXRayLaboratoryProfile(@RequestBody OrganizationProfileDTO organizationProfileDTO) {
        return userService.addXRayLaboratoryProfile(organizationProfileDTO);
    }

    @PostMapping("/add-pharmacy-profile")
    public String addPharmacyProfile(@RequestBody OrganizationProfileDTO organizationProfileDTO) {
        return userService.addPharmacyProfile(organizationProfileDTO);
    }

    @PostMapping("/add-test-lab-profile")
    public String addTestLabProfile(@RequestBody OrganizationProfileDTO organizationProfileDTO) {
        return userService.addTestLabProfile(organizationProfileDTO);
    }

    @PostMapping("/add-profile-image")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        String uploadImage = userService.addProfilePicture(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    @GetMapping("/get-profile-image")
    public ResponseEntity<?> downloadImage() throws IOException {
        byte[] imageData = userService.getProfilePicture();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);

    }

    @PostMapping("/modify-data")
    public ResponseEntity<?> modifyUserData(@RequestBody UserModifyDTO userModifyDTO) {
        return userService.modifyUserData(userModifyDTO);
    }

    @GetMapping("/get-image")
    public ResponseEntity<?> getImage(@RequestParam String username) throws IOException {
        byte[] imageData = userService.getPic(username);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

    @GetMapping("/get-general-info-of-user")
    public GeneralInfoOfUserDTO getGeneralInfoOfUser() {
        return userService.getGeneralInfoOfUser();
    }
}
