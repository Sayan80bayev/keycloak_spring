package com.example.keycloakapp.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
