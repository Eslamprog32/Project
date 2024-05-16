package com.example.lastone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authRequest -> {
            /*
            authRequest.requestMatchers("users/add-patient-profile", "users/add-doctor-profile", "users/get", "users/username" ,
                    "users/add-xray-laboratory-profile").authenticated();

             */
            authRequest.requestMatchers("users/auth/**").authenticated();
            authRequest.requestMatchers("users/Register/**").permitAll();
            authRequest.requestMatchers("doctors/**").hasRole("DOCTOR");
            authRequest.requestMatchers("patients/**").hasRole("PATIENT");
        });
        http.cors(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(AbstractHttpConfigurer::disable);
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }


}

