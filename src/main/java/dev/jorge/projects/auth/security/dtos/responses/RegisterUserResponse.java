package dev.jorge.projects.auth.security.dtos.responses;

import dev.jorge.projects.auth.user.entities.User;

public record RegisterUserResponse(
        String firstName,
        String lastName,
        String username,
        String email
)
{
    public static  RegisterUserResponse fromEntity(User user)
    {
        return new RegisterUserResponse(
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
