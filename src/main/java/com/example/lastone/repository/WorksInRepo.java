package com.example.lastone.repository;

import com.example.lastone.model.entity.WorksInEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorksInRepo extends JpaRepository<WorksInEntity, Long> {
    List<WorksInEntity> findAllByUsername(String username);
}
