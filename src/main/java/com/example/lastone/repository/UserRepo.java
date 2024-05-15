package com.example.lastone.repository;

import com.example.lastone.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUsername(String username);


    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByPhone(String phone);
    Optional<UserEntity> findBySSN(String ssn);
}
