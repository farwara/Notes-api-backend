package com.farwa.notesapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/categories/**").hasRole("admin")
                        .requestMatchers("/api/attachments/**").hasAnyRole("premium", "admin")
                        .requestMatchers("/api/notes/search").hasAnyRole("premium", "admin")
                        .requestMatchers("/api/notes/**").hasAnyRole("user", "premium", "admin")
                        .requestMatchers("/api/tags/**").hasAnyRole("user", "premium", "admin")
                        .requestMatchers("/api/reminders/**").hasAnyRole("user", "premium", "admin")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(new KeycloakRoleConverter()))
                );

        return http.build();
    }
}