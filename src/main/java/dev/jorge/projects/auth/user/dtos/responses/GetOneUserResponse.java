package dev.jorge.projects.auth.user.dtos.responses;

import dev.jorge.projects.auth.user.entities.User;
import dev.jorge.projects.auth.user.enums.Role;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record GetOneUserResponse(
        UUID id,
        String firstName,
        String lastName,
        String username,
        String email,
        Set<Role> roles,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
)
{
    public static GetOneUserResponse fromEntity(User user) {
        return new GetOneUserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
