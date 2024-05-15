package com.example.lastone.repository;

import com.example.lastone.model.entity.DoctorPatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorPatientRepo extends JpaRepository<DoctorPatientEntity, Long> {
    boolean existsByDoctorNameAndPatientName(String doctorName, String patientName);

    Optional<DoctorPatientEntity> findByDoctorNameAndPatientName(String doctorName, String patientName);

    List<DoctorPatientEntity> findAllByPatientName(String patientName);
    void deleteByDoctorNameAndPatientName(String doctorName, String patientName);
}
