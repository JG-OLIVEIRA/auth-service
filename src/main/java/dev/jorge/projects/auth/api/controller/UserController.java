package dev.jorge.projects.auth.api.controller;

import dev.jorge.projects.auth.api.config.JWTUserData;
import dev.jorge.projects.auth.api.dto.response.UserProfileResponse;
import dev.jorge.projects.auth.api.entity.User;
import dev.jorge.projects.auth.api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserProfileResponse>> getCurrentUser(@AuthenticationPrincipal JWTUserData tokenData) {
        List<User> users = userService.getAllUsers();

        List<UserProfileResponse> response = users.stream()
                .map(UserProfileResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }
}
