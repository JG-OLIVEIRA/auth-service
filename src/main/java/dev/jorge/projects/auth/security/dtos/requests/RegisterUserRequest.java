package dev.jorge.projects.auth.security.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterUserRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String username,
        @NotBlank @Email String email,
        @NotBlank String password
) { }
