package dev.jorge.projects.auth.user.factory;

import dev.jorge.projects.auth.user.entities.User;

import java.util.UUID;

public class UserEntityFactory {
    public static User build(UUID userId) {
        User user = new User();
        user.setUserId(userId);
        user.setFirstName("Jorge");
        user.setLastName("Gonçalves de Oliveira");
        user.setUsername("jorge.gdoliveira8");
        user.setEmail("jorge.gdoliveira8@gmail.com");
        user.setPassword("password");

        return user;
    }
}
