package com.example.lastone.service.impl;

import com.example.lastone.model.dto.AuthenticationResponseDTO;
import com.example.lastone.model.dto.LoginRequestDTO;
import com.example.lastone.model.dto.UserRegisterDTO;
import com.example.lastone.model.entity.UserEntity;
import com.example.lastone.model.mapper.UserMapper;
import com.example.lastone.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDTO userRegister(UserRegisterDTO userRegisterDTO) throws Exception {
        Optional<UserEntity> entityByUserName = userRepo.findByUsername(userRegisterDTO.getUsername());
        Optional<UserEntity> entityByEmail = userRepo.findByEmail(userRegisterDTO.getEmail());
        Optional<UserEntity> entityByPhone = userRepo.findByPhone(userRegisterDTO.getPhone());
        Optional<UserEntity> entityBySSN = userRepo.findBySSN(userRegisterDTO.getSSN());
        if (entityByUserName.isPresent()) {
            throw new Exception("UserName already used!");
        }
        if (entityBySSN.isPresent()) {
            throw new Exception("SSN already used!");
        }
        if (entityByEmail.isPresent()) {
            throw new Exception("Email already used!");
        }
        if (entityByPhone.isPresent()) {
            throw new Exception("Phone already used!");
        }
        UserEntity userEntity = userMapper.toEntity(userRegisterDTO);
        userEntity.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        userEntity.setRole("");
        userRepo.save(userEntity);
        return new AuthenticationResponseDTO(jwtService.generateToken(userEntity));
    }

    public AuthenticationResponseDTO login(LoginRequestDTO loginRequestDTO){
        authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(
                  loginRequestDTO.getUsername(),loginRequestDTO.getPassword()
                )
        );
        UserEntity user = userRepo.findByUsername(loginRequestDTO.getUsername()).orElseThrow();
        return new AuthenticationResponseDTO(jwtService.generateToken(user));
    }
}
