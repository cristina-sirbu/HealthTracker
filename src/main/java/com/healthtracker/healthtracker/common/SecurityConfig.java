package com.healthtracker.healthtracker.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())          // simplify for now
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/register", "/users/login", "/h2-console/**").permitAll()
                        .anyRequest().permitAll()           // allow everything for now
                )
                .headers(headers -> headers.frameOptions(frame -> frame.disable())); // allow H2 console frames

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
