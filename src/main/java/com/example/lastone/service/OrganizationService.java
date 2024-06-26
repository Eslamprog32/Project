package com.example.lastone.service;

import com.example.lastone.model.dto.OrganizationProfileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OrganizationService {
    int canAccessThisLab(String organizationName);

    String addEmployee(String username, String pharmacistName);

    String removeEmployee(String username, String pharmacistName);

    String addProfilePicture(String pharmacistName, MultipartFile file) throws IOException;

    byte[] getProfilePicture(String pharmacistName) throws IOException;

    String editOrganizationData(OrganizationProfileDTO profileDTO);
}
