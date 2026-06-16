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
                        .requestMatchers("/api/categories/**").hasRole("ADMIN")
                        .requestMatchers("/api/attachments/**").hasAnyRole("PREMIUM", "ADMIN")
                        .requestMatchers("/api/notes/search").hasAnyRole("PREMIUM", "ADMIN")
                        .requestMatchers("/api/notes/**").hasAnyRole("USER", "PREMIUM", "ADMIN")
                        .requestMatchers("/api/tags/**").hasAnyRole("USER", "PREMIUM", "ADMIN")
                        .requestMatchers("/api/reminders/**").hasAnyRole("USER", "PREMIUM", "ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}