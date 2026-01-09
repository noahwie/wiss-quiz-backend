package com.wiss.quizbackend.service;

import com.wiss.quizbackend.entity.AppUser;
import com.wiss.quizbackend.entity.Role;
import com.wiss.quizbackend.repository.AppUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class AppUserService {
    // Dependencies via Constructor Injection
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser registerUser(String username, String email, String rawPassword, Role role) {
        // Validation 1: Username bereits vergeben?
        if (appUserRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username " + username + " is already in use");
        }

        // Validation 2: Email bereits registriert?
        if (appUserRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email " + email + " is already in use");
        }

        // Password hashen - NIEMALS Klartext speichern!
        String hashedPassword = passwordEncoder.encode(rawPassword);

        // User Entity erstellen
        AppUser newUser = new AppUser(username, email, hashedPassword, role);

        // Speichern und zurückgeben (mit generierter ID)
        return appUserRepository.save(newUser);

    }

    public Optional<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    public Optional<AppUser> authenticateUser(String username, String rawPassword) {
        // User suchen
        Optional<AppUser> userOpt = appUserRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            AppUser user = userOpt.get();
            //Password prüfen mit BCrypt
            //BCrypt vergleicht automatisch mit dem gespeicherten Salt
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return userOpt; // Login erfolgreich
            }
        }
        // Login fehlgeschlagen
        return Optional.empty();
    }

    private boolean isValidEmail(String email) {
        // Einfache Regex für Email-validation
        return email != null &&
                email.contains("@") &&
                email.length() > 3;
    }
}
