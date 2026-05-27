package dev.jorge.projects.auth.user.controllers;

import dev.jorge.projects.auth.user.entities.User;
import dev.jorge.projects.auth.user.factory.UserEntityFactory;
import dev.jorge.projects.auth.user.factory.UserResponseFactory;
import dev.jorge.projects.auth.user.services.UserService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatusCode;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private static final UUID USER_ID = UUID.randomUUID();
    private static final int PAGE = 0;
    private static final int PAGE_SIZE = 10;

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    @Captor
    ArgumentCaptor<PageRequest> pageRequestCaptor;

    @Captor
    ArgumentCaptor<UUID> userIdCaptor;

    @Nested
    class GetOneUser {

        @Test
        void shouldReturnHttpOk() {
            when(userService.findById(any()))
                    .thenReturn(UserEntityFactory.build(USER_ID));

            var response = userController.getOneUser(USER_ID);

            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        }

        @Test
        void shouldPassCorrectParametersToService() {
            when(userService.findById(userIdCaptor.capture()))
                    .thenReturn(UserEntityFactory.build(USER_ID));

            var response = userController.getOneUser(USER_ID);

            assertEquals(USER_ID, userIdCaptor.getValue());
        }

        @Test
        void shouldReturnResponseBodyCorrectly() {
            User user = UserEntityFactory.build(USER_ID);

            when(userService.findById(any()))
                    .thenReturn(user);

            var response = userController.getOneUser(USER_ID);

            assertNotNull(response.getBody());
            assertNotNull(response.getBody().getContent());

            assertEquals(user.getId(), response.getBody().getContent().id());
            assertEquals(user.getFirstName(), response.getBody().getContent().firstName());
            assertEquals(user.getLastName(), response.getBody().getContent().lastName());
            assertEquals(user.getUsername(), response.getBody().getContent().username());
            assertEquals(user.getEmail(), response.getBody().getContent().email());
            assertEquals(user.getRoles(), response.getBody().getContent().roles());
            assertEquals(user.getCreatedAt(), response.getBody().getContent().createdAt());
            assertEquals(user.getUpdatedAt(), response.getBody().getContent().updatedAt());
        }
    }

    @Nested
    class GetAllUsers {

        @Test
        void shouldReturnHttpOk() {
            when(userService.findAll(any()))
                    .thenReturn(UserResponseFactory.build());

            var response = userController.getAllUsers(PAGE, PAGE_SIZE);

            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        }

        @Test
        void shouldPassCorrectParametersToService() {
            when(userService.findAll(pageRequestCaptor.capture()))
                    .thenReturn(UserResponseFactory.build());

            var response = userController.getAllUsers(PAGE, PAGE_SIZE);

            assertEquals(PAGE, pageRequestCaptor.getValue().getPageNumber());
            assertEquals(PAGE_SIZE, pageRequestCaptor.getValue().getPageSize());
        }

        @Test
        void shouldReturnResponseBodyCorrectly() {
            var pagination = UserResponseFactory.build();

            when(userService.findAll(any()))
                    .thenReturn(UserResponseFactory.build());

            var response = userController.getAllUsers(PAGE, PAGE_SIZE);

            assertNotNull(response.getBody());
            assertNotNull(response.getBody().getContent());
            assertNotNull(response.getBody().getContent().content());

            assertEquals(pagination.getNumber(), response.getBody().getContent().page());
            assertEquals(pagination.getSize(), response.getBody().getContent().pageSize());
            assertEquals(pagination.getTotalElements(), response.getBody().getContent().totalElements());
            assertEquals(pagination.getTotalPages(), response.getBody().getContent().totalPages());
        }
    }

}
