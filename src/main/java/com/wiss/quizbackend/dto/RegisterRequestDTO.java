package com.wiss.quizbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {
    @NotBlank(message = "Username ist erforderlich")
    @Size(min = 3, max = 50, message = "Username muss 3-50 zeichen sein")
    private String username;

    @NotBlank(message = "Email ist erforderlich")
    @Email(message = "Email muss gültig sein")
    private String email;

    @NotBlank(message = "Passwort erforderlich")
    @Size (min = 6, message = "passwort muss mindestens 6 Zeichen haben")
    private String password;

    // Default constructor or JSON Deserialization
    public RegisterRequestDTO() {}

}
