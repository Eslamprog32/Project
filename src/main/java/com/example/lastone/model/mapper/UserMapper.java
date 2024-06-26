package com.example.lastone.model.mapper;

import com.example.lastone.model.dto.*;
import com.example.lastone.model.entity.UserEntity;
import org.mapstruct.Mapper;

import java.io.IOException;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(UserRegisterDTO userRegisterDTO);

    PatientViewToDoctorDTO toPatientViewToDoctorDTO(UserEntity userEntity) throws IOException;

    DoctorViewToPatientDTO toDoctorViewToPatientDTO(UserEntity userEntity);
    PatientViewAsListDTO toPatientViewToDoctorAsListDTO(UserEntity userEntity);
    GeneralInfoOfUserDTO toGeneralInfoOfUserDto(UserEntity userEntity);
}
