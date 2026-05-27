package dev.jorge.projects.auth.security.controllers;

import dev.jorge.projects.auth.security.factory.AuthRequestFactory;
import dev.jorge.projects.auth.security.services.AuthService;
import dev.jorge.projects.auth.user.factory.UserEntityFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private static final String TOKEN = "token";
    private static final UUID USER_ID = UUID.randomUUID();
    private static final String FIRSTNAME = "Jorge";
    private static final String LASTNAME = "Gonçalves de Oliveira";
    private static final String USERNAME = "jorge.gdoliveira8";
    private static final String EMAIL = "jorge.gdoliveira8@gmail.com";
    private static final String PASSWORD = "password";

    @Mock
    AuthService authService;

    @InjectMocks
    AuthController authController;

    @Captor
    ArgumentCaptor<String> firstNameCaptor;

    @Captor
    ArgumentCaptor<String> lastNameCaptor;

    @Captor
    ArgumentCaptor<String> usernameCaptor;

    @Captor
    ArgumentCaptor<String> emailCaptor;

    @Captor
    ArgumentCaptor<String> passwordCaptor;

    @Nested
    class Login {

        @Test
        void shouldReturnHttpOk() {
            when(authService.signIn(anyString(), anyString()))
                    .thenReturn(TOKEN);

            var response = authController.login(AuthRequestFactory.buildLoginRequest());

            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        }

        @Test
        void shouldPassCorrectParametersToService() {
            when(authService.signIn(emailCaptor.capture(), passwordCaptor.capture()))
                    .thenReturn(TOKEN);

            var response = authController.login(AuthRequestFactory.buildLoginRequest());

            assertEquals(EMAIL, emailCaptor.getValue());
            assertEquals(PASSWORD, passwordCaptor.getValue());
        }

        @Test
        void shouldReturnResponseBodyCorrectly() {
            when(authService.signIn(anyString(), anyString()))
                    .thenReturn(TOKEN);

            var response = authController.login(AuthRequestFactory.buildLoginRequest());

            assertNotNull(response.getBody());

            assertEquals(TOKEN, response.getBody().token());
        }
    }

    @Nested
    class Register {

        @Test
        void shouldReturnHttpOk() {
            when(authService.signUp(anyString(), anyString(), anyString(), anyString(), anyString()))
                    .thenReturn(UserEntityFactory.build(USER_ID));

            var response = authController.register(AuthRequestFactory.buildRegisterUserRequest());

            assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        }

        @Test
        void shouldPassCorrectParametersToService() {
            when(authService.signUp(firstNameCaptor.capture(), lastNameCaptor.capture(), usernameCaptor.capture(), emailCaptor.capture(), passwordCaptor.capture()))
                    .thenReturn(UserEntityFactory.build(USER_ID));

            var response = authController.register(AuthRequestFactory.buildRegisterUserRequest());

            assertEquals(FIRSTNAME, firstNameCaptor.getValue());
            assertEquals(LASTNAME, lastNameCaptor.getValue());
            assertEquals(USERNAME, usernameCaptor.getValue());
            assertEquals(EMAIL, emailCaptor.getValue());
            assertEquals(PASSWORD, passwordCaptor.getValue());
        }

        @Test
        void shouldReturnResponseBodyCorrectly() {
            when(authService.signIn(anyString(), anyString()))
                    .thenReturn(TOKEN);

            var response = authController.login(AuthRequestFactory.buildLoginRequest());

            assertNotNull(response.getBody());

            assertEquals(TOKEN, response.getBody().token());
        }
    }
}
