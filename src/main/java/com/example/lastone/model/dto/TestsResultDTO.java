package com.example.lastone.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestsResultDTO {
    private String patientName;
    private String value;
    private String unites;
    private String category;
    private String code;
    private Boolean is_abnormal;
    private String description;
}
