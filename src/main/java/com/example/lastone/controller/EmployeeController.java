package com.example.lastone.controller;


import com.example.lastone.model.entity.DoctorEntity;
import com.example.lastone.model.entity.PatientEntity;
import com.example.lastone.repository.DoctorRepo;
import com.example.lastone.repository.PatientRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
//@RequestMapping("pateints")
//@OpenAPI30
//@Slf4j
//@Profile("dev")
//@Profile(value = {"dev","local"})
public class EmployeeController {


    private final PatientRepo patientRepo;
    private final DoctorRepo doctorRepo;

    @GetMapping("hello")
    public String Hello() {
        return "HomePage";

    }

    @GetMapping("get-doctors")
    public List<PatientEntity> getDoctors() {
        return patientRepo.findAll();
    }

    @GetMapping("get-pateints")
    public List<DoctorEntity> getPatients() {
        return doctorRepo.findAll();
    }

}



