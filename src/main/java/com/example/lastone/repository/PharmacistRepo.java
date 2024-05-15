package com.example.lastone.repository;

import com.example.lastone.model.entity.PatientEntity;
import com.example.lastone.model.entity.PharmacistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacistRepo extends JpaRepository<PharmacistEntity, String> {

}
