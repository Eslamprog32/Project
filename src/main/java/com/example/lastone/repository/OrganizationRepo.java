package com.example.lastone.repository;

import com.example.lastone.model.entity.OrganizationEntity;
import com.example.lastone.model.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepo extends JpaRepository<OrganizationEntity,String> {
}
