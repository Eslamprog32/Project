package com.example.lastone.model.mapper;

import com.example.lastone.model.dto.PatientViewToDoctorDTO;
import com.example.lastone.model.entity.PatientEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientViewToDoctorDTO toPatientViewToDoctorDTO(PatientEntity patientEntity);
}
