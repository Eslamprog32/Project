package com.example.lastone.repository;

import com.example.lastone.model.entity.VerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationRepo extends JpaRepository<VerificationEntity, Long> {
    VerificationEntity findByToken(String token);
}
