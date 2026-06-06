package dev.jorge.projects.auth.dto.response;

import dev.jorge.projects.auth.model.User;

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
