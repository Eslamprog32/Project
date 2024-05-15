package com.example.lastone.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientViewToDoctorDTO {
    private String username;
    private String fullName;
    private String phone;
    private String gender;
    private String dateOfBirth;
    private String martalStatus;
    private String profilePicture;
}
