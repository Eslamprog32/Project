package com.example.lastone.controller;

import com.example.lastone.Exceptions.MessageError;
import com.example.lastone.model.dto.AuthenticationResponseDTO;
import com.example.lastone.model.dto.LoginRequestDTO;
import com.example.lastone.model.dto.UserRegisterDTO;
import com.example.lastone.model.entity.UserEntity;
import com.example.lastone.model.entity.VerificationEntity;
import com.example.lastone.service.UserService;
import com.example.lastone.service.impl.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO userRegisterDTO) throws Exception {
        try {
            String response = authenticationService.userRegister(userRegisterDTO);
            return ResponseEntity.ok(response);
        } catch (MessageError ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String token) {
        VerificationEntity verificationToken = authenticationService.getVerificationToken(token);
        if (verificationToken == null || verificationToken.getExpiryDate().before(new Date())) {
            return "Invalid or expired token.";
        }
        UserEntity user = authenticationService.getUserEntity(verificationToken.getUsername());
        authenticationService.verifyUser(user);
        return "Email successfully verified.";
    }

    @PostMapping("/login")
    public AuthenticationResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return authenticationService.login(loginRequestDTO);
    }
}
