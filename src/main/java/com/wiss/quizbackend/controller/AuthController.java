package com.wiss.quizbackend.controller;

import com.wiss.quizbackend.dto.LoginRequestDTO;
import com.wiss.quizbackend.dto.LoginResponseDTO;
import com.wiss.quizbackend.dto.RegisterRequestDTO;
import com.wiss.quizbackend.dto.RegisterResponseDTO;
import com.wiss.quizbackend.entity.AppUser;
import com.wiss.quizbackend.entity.Role;
import com.wiss.quizbackend.service.AppUserService;
import com.wiss.quizbackend.service.JwtService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    private final AppUserService appUserService;
    private final JwtService jwtService;
    public AuthController(AppUserService appUserService, JwtService jwtService) {
        this.appUserService = appUserService;
        this.jwtService =  jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO request) {
        try {
            AppUser newUser = appUserService.registerUser(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword(),
                    Role.PLAYER
            );

            RegisterResponseDTO response = new RegisterResponseDTO(
                    newUser.getId(),
                    newUser.getUsername(),
                    newUser.getEmail(),
                    newUser.getRole().name()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Registrierung fehlgeschlagen");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth Controller funktioniert!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            // 1. User finden (Username oder Email)
            Optional<AppUser> userOpt;

            // Prüfen ob Email oder Username
            if (request.getUsernameOrEmail().contains("@")) {
                // Hat @? -> Email
                userOpt = appUserService.findByEmail(request.getUsernameOrEmail());
            } else {
                // kein @? -> Username
                userOpt = appUserService.findByUsername(request.getUsernameOrEmail());
            }

            // User existiert nicht
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Ungültige Anmeldedaten"));
            }

            AppUser user = userOpt.get();

            // 2. Passwort prüfen mit authenticateUser
            Optional<AppUser> authenticatedUser =
                    appUserService.authenticateUser(user.getUsername(), user.getPassword());

            if (authenticatedUser.isEmpty()) {
                // passwort falsch
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Ungültige Anmeldedaten"));
            }

            // 3. JWT Token generieren
            String token = jwtService.generateToken(
                    user.getUsername(),
                    user.getRole().name()
            );

            // 4. Response DTO erstellen
            LoginResponseDTO response = new LoginResponseDTO(
                    token,
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole().name(),
                    86400000L
            );

            // 5. Success Response
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // unerwartete Fehler
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Ein Fehler ist aufgetret" + e.getMessage()));
        }
    }
}
