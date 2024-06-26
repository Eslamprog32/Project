package com.example.lastone.controller;

import com.example.lastone.service.impl.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final EmailService emailService;
    @GetMapping("/mytest")
    public String mytest() {
        emailService.sendSimpleMessage("eslamgamil6@gmail.com", "It's Working", "WOW");
        return "Done";
    }
}
