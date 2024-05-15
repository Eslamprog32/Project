package com.example.lastone.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserRegisterDTO {
    private String username;
    private String SSN;
    private String fullName;
    private String password;
    private String phone;
    private String email;
    private String gender;
    private String dateOfBirth;
    private String address;
    private String martalStatus;
    private String profilePicture;

}
