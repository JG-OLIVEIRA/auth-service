package dev.jorge.projects.auth.user.dto.response;

import dev.jorge.projects.auth.user.entity.User;
import dev.jorge.projects.auth.user.enums.Role;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record GetUserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        Set<Role> roles,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
)
{
    public static GetUserResponse fromEntity(User user) {
        return new GetUserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRoles(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
