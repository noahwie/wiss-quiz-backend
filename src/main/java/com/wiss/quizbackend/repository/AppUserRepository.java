package com.wiss.quizbackend.repository;

import com.wiss.quizbackend.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    // Custom Query Methods for User Management
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);

    // For Login Validation
    Optional<AppUser> findByEmailAndPassword(String email, String password);

    // Checks if username and email already exists
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
