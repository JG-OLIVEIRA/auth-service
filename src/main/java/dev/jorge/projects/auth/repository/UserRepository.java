package dev.jorge.projects.auth.repository;

import dev.jorge.projects.auth.model.User;
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
