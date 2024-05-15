package com.example.lastone.repository;

import com.example.lastone.model.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepo extends JpaRepository<PatientEntity,String> {

}
