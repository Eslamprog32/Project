package com.example.lastone.controller;

import com.example.lastone.model.dto.OrganizationProfileDTO;
import com.example.lastone.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/Organization")
@RestController
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;

    @PostMapping("/add-employee")
    public String addEmployee(@RequestParam String username, @RequestParam String organizationName) {
        return organizationService.addEmployee(username, organizationName);
    }

    @PostMapping("/remove-employee")
    public String removeEmployee(@RequestParam String username, @RequestParam String organizationName) {
        return organizationService.removeEmployee(username, organizationName);
    }

    @PostMapping("/add-profile-picture")
    public String addProfilePicture(@RequestParam String organizationName
            , @RequestParam("image") MultipartFile file) throws IOException {
        return organizationService.addProfilePicture(organizationName, file);
    }

    @GetMapping("/get-profile-picture")
    public ResponseEntity<?> getProfilePicture(@RequestParam String organizationName) throws IOException {
        byte[] image = organizationService.getProfilePicture(organizationName);
        return ResponseEntity.status(HttpStatus.OK).contentType
                (MediaType.valueOf("image/png")).body(image);
    }

    @PostMapping("/edit-profile-data")
    public String editOrganizationData(@RequestBody OrganizationProfileDTO profileDTO) {
        return organizationService.editOrganizationData(profileDTO);
    }
}
