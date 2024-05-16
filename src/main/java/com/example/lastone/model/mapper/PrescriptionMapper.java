package com.example.lastone.model.mapper;

import com.example.lastone.model.dto.*;
import com.example.lastone.model.entity.MedicineInPrescriptionEntity;
import com.example.lastone.model.entity.PrescriptionEntity;
import com.example.lastone.model.entity.TestsInPrescriptionEntity;
import com.example.lastone.model.entity.XRayInPrescriptionEntity;
import com.example.lastone.repository.MedicineInPrescriptionRepo;
import com.example.lastone.repository.TestsInPrescriptionRepo;
import com.example.lastone.repository.XRayInPrescriptionRepo;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {
    PrescriptionEntity toPrescriptionEntity(PrescriptionAddFromDoctorDTO prescriptionAddFromDoctorDTO);

}
