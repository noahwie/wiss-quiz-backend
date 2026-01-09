package com.wiss.quizbackend.dto;

import lombok.Getter;

@Getter
public class RegisterResponseDTO {
    private final Long id;
    private final String username;
    private final String email;
    private final String role;
    private final String message;

    public RegisterResponseDTO(Long id, String username, String email, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.message = "Registration erfolgreich!";
    }

}
