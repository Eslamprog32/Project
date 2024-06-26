package com.example.lastone.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserModifyDTO {
    private String fullName;
    private String oldPassword;
    private String newPassword;
    private String phone;
    private String gender;
    private LocalDate dateOfBirth;
    private String address;
    private String martalStatus;
}
