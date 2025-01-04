package com.example.keycloakapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.AbstractConfiguredSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/login").anonymous() // Открытые эндпоинты
                        .anyRequest().authenticated()             // Защита остальных
                )
                .csrf( AbstractHttpConfigurer::disable)
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("http://localhost:8080/oauth2/authorization/keycloak") // URL для авторизации через Keycloak
                )
                .oauth2Client(client -> {
                    // Здесь можно настроить OAuth2 Client, если нужно
                })
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(jwtDecoder()) // Настройка JWT декодера
                        )
                );

        return http.build();
    }

    @Bean
    public NimbusJwtDecoder jwtDecoder() {
        String issuerUri = "http://localhost:8080/realms/master"; // Ваш issuer (URI из Keycloak)
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri);

        // Добавляем валидацию токенов
        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<Jwt>(
                new JwtIssuerValidator(issuerUri),
                new JwtTimestampValidator()
        ));

        return jwtDecoder;
    }
}