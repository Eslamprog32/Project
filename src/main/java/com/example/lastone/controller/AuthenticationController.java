package com.example.lastone.controller;

import com.example.lastone.model.dto.AuthenticationResponseDTO;
import com.example.lastone.model.dto.LoginRequestDTO;
import com.example.lastone.model.dto.UserRegisterDTO;
import com.example.lastone.service.impl.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public AuthenticationResponseDTO register(@RequestBody UserRegisterDTO userRegisterDTO) throws Exception {
        return authenticationService.userRegister(userRegisterDTO);
    }
    @PostMapping("/login")
    public AuthenticationResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return authenticationService.login(loginRequestDTO);
    }
}
