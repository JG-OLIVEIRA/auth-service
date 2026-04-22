package dev.jorge.projects.auth.api.controller;

import dev.jorge.projects.auth.api.config.JWTUserData;
import dev.jorge.projects.auth.api.dto.request.LoginRequest;
import dev.jorge.projects.auth.api.dto.request.RegisterUserRequest;
import dev.jorge.projects.auth.api.dto.response.LoginResponse;
import dev.jorge.projects.auth.api.dto.response.RegisterUserResponse;
import dev.jorge.projects.auth.api.dto.response.UserProfileResponse;
import dev.jorge.projects.auth.api.entity.User;
import dev.jorge.projects.auth.api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        String email = request.email();
        String password = request.password();

        String token = authService.signIn(email, password);

        return ResponseEntity.ok(LoginResponse.fromEntity(token));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(@Valid @RequestBody RegisterUserRequest request){
        String firstName = request.firstName();
        String lastName = request.lastName();
        String email = request.email();
        String password = request.password();

        User newUser = authService.signUp(firstName, lastName, email, password);

        return ResponseEntity.status(HttpStatus.CREATED).body(RegisterUserResponse.fromEntity(newUser));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUser(@AuthenticationPrincipal JWTUserData tokenData) {
        User user = authService.findUserByEmail(tokenData.email());

        return ResponseEntity.ok(UserProfileResponse.fromEntity(user));
    }
}
