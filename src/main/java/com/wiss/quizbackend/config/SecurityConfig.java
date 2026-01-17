package com.wiss.quizbackend.config;

import com.wiss.quizbackend.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 12 = workfactor. goes from 10-12 for standard and 14+ dor high sensitiv data
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // SCHRITT 1: CSRF deaktivieren
                // Warum? Bei JWT nicht nötig, da stateless (kein Cookie)
                .csrf(csrf -> csrf.disable())

                // SCHRITT 2: CORS konfigurieren
                // Warum? Damit unser React Frontend das Backend aufrufen kann
                .cors(cors -> cors.configure(http))

                // SCHRITT 3: Authorization Rules - WER DARF WO REIN?
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        // 🔒 ALLE anderen Endpoints benötigen
                        // einen gültigen Ausweis (JWT)
                        .anyRequest().authenticated()
                )

                // SCHRITT 4: Session Management auf STATELESS setzen
                // Warum? Wir benutzen JWT, nicht Sessions/Cookies
                // Analogie: Keine Besucherliste führen, nur Ausweise prüfen
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
