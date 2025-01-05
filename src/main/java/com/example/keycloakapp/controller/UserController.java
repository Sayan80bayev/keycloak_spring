package com.example.keycloakapp.controller;

import com.example.keycloakapp.dto.request.LoginRequest;
import com.example.keycloakapp.dto.response.LoginResponse;
import com.example.keycloakapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @GetMapping("/secured")
    public String secured() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "secured";
    }
}
