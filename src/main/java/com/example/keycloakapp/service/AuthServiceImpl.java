package com.example.keycloakapp.service;

import com.example.keycloakapp.dto.request.LoginRequest;
import com.example.keycloakapp.dto.response.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private String issuer;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String secret;

    @Value("${spring.security.oauth2.client.registration.keycloak.authorization-grant-type}")
    private String grantType;

    @Value("${keycloak.token.url}")
    private String tokenUrl;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", secret);
        requestBody.add("grant_type", "password");
        requestBody.add("username", loginRequest.getEmail());
        requestBody.add("password", loginRequest.getPassword());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    tokenUrl,
                    request,
                    String.class
            );
            return objectMapper.readValue(response.getBody(), LoginResponse.class);
        } catch (RestClientException e) {
            throw new RuntimeException("Error during login request", e);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing login response", e);
        }
    }
}