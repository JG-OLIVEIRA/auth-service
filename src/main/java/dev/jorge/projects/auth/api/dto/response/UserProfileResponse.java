package dev.jorge.projects.auth.api.dto.response;

import dev.jorge.projects.auth.api.entity.User;
import dev.jorge.projects.auth.api.enums.Role;

import java.util.Set;

public record UserProfileResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Set<Role> roles
)
{
    public static UserProfileResponse fromEntity(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRoles()
        );
    }
}
