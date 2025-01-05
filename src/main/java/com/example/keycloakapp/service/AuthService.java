package com.example.keycloakapp.service;

import com.example.keycloakapp.dto.request.LoginRequest;
import com.example.keycloakapp.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
}
