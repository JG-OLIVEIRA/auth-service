package dev.jorge.projects.auth.user.services;

import dev.jorge.projects.auth.user.entities.User;
import dev.jorge.projects.auth.user.exceptions.UserNotFoundException;
import dev.jorge.projects.auth.user.factory.UserEntityFactory;
import dev.jorge.projects.auth.user.factory.UserResponseFactory;
import dev.jorge.projects.auth.user.repositories.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final UUID USER_ID = UUID.randomUUID();
    private static final int PAGE = 0;
    private static final int PAGE_SIZE = 10;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Nested
    class findById {

        @Test
        void shouldCallRepository() {
            when(userRepository.findById(eq(USER_ID)))
                    .thenReturn(Optional.of(UserEntityFactory.build(USER_ID)));

            var response = userService.findById(USER_ID);

            verify(userRepository, times(1)).findById(eq(USER_ID));
        }

        @Test
        void shouldReturnUser() {
            User user = UserEntityFactory.build(USER_ID);

            when(userRepository.findById(any()))
                    .thenReturn(Optional.of(user));


            var response = userService.findById(USER_ID);

            assertEquals(user.getId(), response.getId());
            assertEquals(user.getFirstName(), response.getFirstName());
            assertEquals(user.getLastName(), response.getLastName());
            assertEquals(user.getUsername(), response.getUsername());
            assertEquals(user.getEmail(), response.getEmail());
            assertEquals(user.getPassword(), response.getPassword());
        }

        @Test
        void shouldThrowUserNotFoundException() {
            when(userRepository.findById(USER_ID))
                    .thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService.findById(USER_ID));

            verify(userRepository).findById(USER_ID);
        }
    }

    @Nested
    class findByAll {

        @Test
        void shouldCallRepository() {
            when(userRepository.findAll(PageRequest.of(PAGE, PAGE_SIZE)))
                    .thenReturn(UserResponseFactory.build());

            var response = userService.findAll(PageRequest.of(PAGE, PAGE_SIZE));

            verify(userRepository, times(1)).findAll(eq(PageRequest.of(PAGE, PAGE_SIZE)));
        }

        @Test
        void shouldReturnPageOfUser() {
            Page<User> user = UserResponseFactory.build();

            when(userRepository.findAll(PageRequest.of(PAGE, PAGE_SIZE)))
                    .thenReturn(user);


            var response = userService.findAll(PageRequest.of(PAGE, PAGE_SIZE));

            assertEquals(user.getContent(), response.getContent());
            assertEquals(user.getNumber(), response.getNumber());
            assertEquals(user.getTotalPages(), response.getTotalPages());
            assertEquals(user.getNumberOfElements(), response.getNumberOfElements());
            assertEquals(user.getTotalElements(), response.getTotalElements());
        }
    }
}
