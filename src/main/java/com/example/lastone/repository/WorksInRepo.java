package com.example.lastone.repository;

import com.example.lastone.model.entity.WorksInEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorksInRepo extends JpaRepository<WorksInEntity, Long> {
    List<WorksInEntity> findAllByUsername(String username);

    Optional<WorksInEntity> findByUsernameAndOrganizationName(String username, String organizationName);

    void deleteByUsernameAndOrganizationName(String username, String organizationName);
}
