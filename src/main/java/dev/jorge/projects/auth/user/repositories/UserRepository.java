package dev.jorge.projects.auth.user.repositories;

import dev.jorge.projects.auth.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
}
