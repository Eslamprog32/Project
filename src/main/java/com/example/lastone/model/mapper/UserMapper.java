package com.example.lastone.model.mapper;

import com.example.lastone.model.dto.DoctorViewToPatientDTO;
import com.example.lastone.model.dto.PatientViewToDoctorDTO;
import com.example.lastone.model.dto.UserRegisterDTO;
import com.example.lastone.model.entity.PatientEntity;
import com.example.lastone.model.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(UserRegisterDTO userRegisterDTO);

    PatientViewToDoctorDTO toPatientViewToDoctorDTO(UserEntity userEntity);

    DoctorViewToPatientDTO toDoctorViewToPatientDTO(UserEntity userEntity);
}
