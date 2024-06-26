package com.example.lastone.model.mapper;

import com.example.lastone.model.dto.*;
import com.example.lastone.model.entity.PrescriptionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {
    PrescriptionEntity toPrescriptionEntity(PrescriptionAddFromDoctorDTO prescriptionAddFromDoctorDTO);

}
