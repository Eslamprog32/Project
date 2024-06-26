package com.example.lastone.service.impl;

import com.example.lastone.Exceptions.MessageError;
import com.example.lastone.model.dto.OrganizationProfileDTO;
import com.example.lastone.model.entity.OrganizationEntity;
import com.example.lastone.model.entity.WorksInEntity;
import com.example.lastone.model.mapper.OrganizationMapper;
import com.example.lastone.repository.OrganizationRepo;
import com.example.lastone.repository.WorksInRepo;
import com.example.lastone.service.OrganizationService;
import com.example.lastone.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrganizationServiceImp implements OrganizationService {
    private final OrganizationRepo organizationRepo;
    private final WorksInRepo worksInRepo;
    private final OrganizationMapper organizationMapper;

    private String getUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @Override
    public int canAccessThisLab(String organizationName) {
        Optional<WorksInEntity> works = worksInRepo.
                findByUsernameAndOrganizationName(getUsername(), organizationName);
        if (works.isPresent()) {
            if (works.get().getAdmin()) {
                return 2;
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }

    @Override
    public String addEmployee(String username, String organizationName) {
        if (canAccessThisLab(organizationName) < 2) {
            throw new MessageError("this Request Require Admin!");
        }
        if (worksInRepo.findByUsernameAndOrganizationName(username, organizationName).isPresent()) {
            throw new MessageError("User Already Work Here!");
        }
        OrganizationEntity organization = organizationRepo.findById(organizationName).get();
        WorksInEntity works = new WorksInEntity();
        works.setUsername(username);
        works.setOrganizationName(organizationName);
        works.setType(organization.getType());
        works.setAdmin(false);
        worksInRepo.save(works);
        return "Add Successfully!";
    }

    @Override
    public String removeEmployee(String username, String pharmacistName) {
        if (canAccessThisLab(pharmacistName) < 2) {
            throw new MessageError("this Request Require Admin!");
        }
        if (worksInRepo.findByUsernameAndOrganizationName(username, pharmacistName).isEmpty()) {
            throw new MessageError("User Already Not Work Here!");
        }
        worksInRepo.deleteByUsernameAndOrganizationName(username, pharmacistName);
        return "Add Successfully!";
    }

    @Override
    public String addProfilePicture(String pharmacistName, MultipartFile file) throws IOException {
        if (canAccessThisLab(pharmacistName) < 2) {
            throw new MessageError("this Request Require Admin!");
        }
        Optional<OrganizationEntity> organization = organizationRepo.findById(pharmacistName);
        if (organization.isEmpty()) {
            throw new MessageError("No Organization With This Name");
        }
        OrganizationEntity organization1 = organization.get();
        organization1.setPicture(ImageUtils.compressImage(file.getBytes()));
        organizationRepo.save(organization1);
        if (organization1.getPicture() != null) {
            return "add image successfully";
        }
        throw new MessageError("something wrong !");
    }

    @Override
    public byte[] getProfilePicture(String pharmacistName) throws IOException {
        if (canAccessThisLab(pharmacistName) == 0) {
            throw new MessageError("User Can't Access This Lab!");
        }
        Optional<OrganizationEntity> organization = organizationRepo.findById(pharmacistName);
        if (organization.isEmpty()) {
            throw new MessageError("No Organization With This Name");
        }
        if (organization.get().getPicture() == null) {
            throw new MessageError("No ProfilePicture!");
        }
        byte[] images = ImageUtils.decompressImage(organization.get().getPicture());
        return images;
    }

    @Override
    public String editOrganizationData(OrganizationProfileDTO profileDTO) {
        if (canAccessThisLab(profileDTO.getOrganizationName()) < 2) {
            throw new MessageError("User Can't Access This Organization!");
        }
        OrganizationEntity organization = new OrganizationEntity();
        organization.setOrganizationName(profileDTO.getOrganizationName());
        if (profileDTO.getEmail() != null) {
            organization.setEmail(profileDTO.getEmail());
        }
        if (profileDTO.getPhone() != null) {
            organization.setPhone(profileDTO.getPhone());
        }
        if (profileDTO.getLocation() != null) {
            organization.setLocation(profileDTO.getLocation());
        }
        return "Successfully Updated";
    }
}
