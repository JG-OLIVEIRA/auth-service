package dev.jorge.projects.auth.api.dto.response;

import dev.jorge.projects.auth.api.entity.User;

public record RegisterUserResponse(String firstName, String lastName, String email)
{
    public static RegisterUserResponse fromEntity(User user) {
        return new RegisterUserResponse(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
}
