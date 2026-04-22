package dev.jorge.projects.auth.api.repository;

import dev.jorge.projects.auth.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String username);
    Boolean existsByEmail(String email);
}
