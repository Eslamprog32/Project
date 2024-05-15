package com.example.lastone.model.mapper;

import com.example.lastone.model.dto.DoctorViewToPatientDTO;
import com.example.lastone.model.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorViewToPatientDTO toDoctorViewToPatientDTO(UserEntity doctor);

}
