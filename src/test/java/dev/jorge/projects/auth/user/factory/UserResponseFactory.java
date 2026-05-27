package dev.jorge.projects.auth.user.factory;

import dev.jorge.projects.auth.user.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class UserResponseFactory {

    public static Page<User> build() {
        User user = new User();
        user.setFirstName("Jorge");
        user.setLastName("Gonçalves de Oliveira");
        user.setUsername("jorge.gdoliveira8");
        user.setEmail("jorge.gdoliveira8@gmail.com");
        user.setPassword("senhatest");

        return new PageImpl<>(
                List.of(user),
                PageRequest.of(0, 10),
                1
        );
    }

}
