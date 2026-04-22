package dev.jorge.projects.auth.api.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record RegisterUserRequest (
        @NotEmpty(message = "Nome é obrigatório") String firstName,
        @NotEmpty(message = "Nome é obrigatório") String lastName,
        @NotEmpty(message = "Email é obrigatório") String email,
        @NotEmpty(message = "Senha é obrigatória") String password) {
}
