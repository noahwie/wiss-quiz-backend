package com.wiss.quizbackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    @NotBlank(message = "Username oder Email ist erforderlich")
    private String usernameOrEmail;

    @NotBlank(message = "Passwort ist erforderlich")
    private String passwort;

    public LoginRequestDTO() {}

    public LoginRequestDTO(String usernameOrEmail, String passwort) {
        this.usernameOrEmail = usernameOrEmail;
        this.passwort = passwort;
    }

    @Override
    public String toString() {
        return "LoginRequestDTO{" +
                "usernameOrEmail='" + usernameOrEmail + '\'' +
                ", password='[HIDDEN]'" +
                '}';
    }
}
