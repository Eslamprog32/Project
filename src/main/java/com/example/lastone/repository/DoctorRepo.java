package com.example.lastone.repository;

import com.example.lastone.model.entity.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepo extends JpaRepository<DoctorEntity,String> {
   Boolean existsByDoctorName(String doctorName);
}
