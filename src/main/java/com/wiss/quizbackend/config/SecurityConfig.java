package com.wiss.quizbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 12 = workfactor. goes from 10-12 for standard and 14+ dor high sensitiv data
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF für REST APIs deaktivieren
                // (verwenden von JWT stattdessen)
                .csrf(csrf -> csrf.disable())
                //request authorization rules
                .authorizeRequests(auth -> auth
                        // auth endpoints müssen öffentlich sein
                        .requestMatchers("/api/auth/**").permitAll()
                        // Swagger UI für API dokumentation
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        // TEMPORÄR: alle anderen Requests erlauben
                        // TODO: nach JWT implementation -> .authenticated()
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}
