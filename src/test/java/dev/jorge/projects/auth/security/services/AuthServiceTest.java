package dev.jorge.projects.auth.security.services;

import dev.jorge.projects.auth.security.configs.TokenConfig;
import dev.jorge.projects.auth.security.producers.AuthProducer;
import dev.jorge.projects.auth.user.entities.User;
import dev.jorge.projects.auth.user.enums.Role;
import dev.jorge.projects.auth.user.exceptions.UserAlreadyExistsException;
import dev.jorge.projects.auth.user.exceptions.UserNotFoundException;
import dev.jorge.projects.auth.user.factory.UserEntityFactory;
import dev.jorge.projects.auth.user.repositories.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    private static final UUID USER_ID = UUID.randomUUID();
    private static final String FIRST_NAME = "Jorge";
    private static final String LAST_NAME = "Gonçalves de Oliveira";
    private static final String USERNAME = "jorge.gdoliveira8";
    private static final String EMAIL = "jorge.gdoliveira8@gmail.com";
    private static final String PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encoded-password";
    private static final String EXPECTED_TOKEN = "generated-token";

    @Mock
    UserRepository userRepository;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    TokenConfig tokenConfig;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AuthProducer authProducer;

    @InjectMocks
    AuthService authService;

    @Nested
    class SignIn {

        @Test
        void shouldReturnTokenWhenCredentialsAreValid() {
            Authentication authMock = mock(Authentication.class);
            when(authMock.isAuthenticated()).thenReturn(true);

            when(userRepository.findUserByEmail(EMAIL))
                    .thenReturn(Optional.of(UserEntityFactory.build(USER_ID)));
            when(authenticationManager.authenticate(any()))
                    .thenReturn(authMock);
            when(tokenConfig.generateSessionToken(any()))
                    .thenReturn(EXPECTED_TOKEN);

            var response = authService.signIn(EMAIL, PASSWORD);

            assertNotNull(response);
            verify(userRepository, times(1)).findUserByEmail(EMAIL);
            verify(authenticationManager, times(1)).authenticate(any());
        }

        @Test
        void shouldThrowBadCredentialsExceptionWhenPasswordIsWrong() {
            when(userRepository.findUserByEmail(EMAIL))
                    .thenReturn(Optional.of(UserEntityFactory.build(USER_ID)));
            when(authenticationManager.authenticate(any()))
                    .thenThrow(new BadCredentialsException("Credenciais inválidas"));

            assertThrows(BadCredentialsException.class,
                    () -> authService.signIn(EMAIL, PASSWORD));
        }

        @Test
        void shouldThrowWhenUserNotFound() {
            when(userRepository.findUserByEmail(EMAIL))
                    .thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class,
                    () -> authService.signIn(EMAIL, PASSWORD));
        }
    }

    @Nested
    class SignUp {

        @Test
        void shouldCreateUserSuccessfully() {
            when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
            when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
            when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
            when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
            doNothing().when(authProducer).publishMessageEmail(any(User.class));

            User response = authService.signUp(FIRST_NAME, LAST_NAME, USERNAME, EMAIL, PASSWORD);

            assertNotNull(response);
            assertEquals(EMAIL, response.getEmail());
            assertEquals(USERNAME, response.getUsername());
            assertEquals(FIRST_NAME, response.getFirstName());
            assertEquals(LAST_NAME, response.getLastName());
            assertTrue(response.getRoles().contains(Role.ROLE_USER));
            verify(userRepository, times(1)).save(any(User.class));
        }

        @Test
        void shouldThrowWhenEmailAlreadyExists() {
            when(userRepository.existsByEmail(EMAIL)).thenReturn(true);

            assertThrows(UserAlreadyExistsException.class,
                    () -> authService.signUp(FIRST_NAME, LAST_NAME, USERNAME, EMAIL, PASSWORD));

            verify(userRepository, never()).save(any());
        }

        @Test
        void shouldThrowWhenUsernameAlreadyExists() {
            when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
            when(userRepository.existsByUsername(USERNAME)).thenReturn(true);

            assertThrows(UserAlreadyExistsException.class,
                    () -> authService.signUp(FIRST_NAME, LAST_NAME, USERNAME, EMAIL, PASSWORD));

            verify(userRepository, never()).save(any());
        }
    }

}
