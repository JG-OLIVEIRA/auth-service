package dev.jorge.projects.auth.security.controller;

import dev.jorge.projects.auth.security.config.JWTUserData;
import dev.jorge.projects.auth.security.dto.request.LoginRequest;
import dev.jorge.projects.auth.user.dto.request.CreateUserRequest;
import dev.jorge.projects.auth.security.dto.response.LoginResponse;
import dev.jorge.projects.auth.user.dto.response.CreateUserResponse;
import dev.jorge.projects.auth.user.dto.response.GetUserResponse;
import dev.jorge.projects.auth.user.entity.User;
import dev.jorge.projects.auth.security.service.AuthService;
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
        String token = authService.signIn(request.email(), request.password());
        return ResponseEntity.ok(LoginResponse.fromEntity(token));
    }

    @PostMapping("/register")
    public ResponseEntity<CreateUserResponse> register(@Valid @RequestBody CreateUserRequest request){
        User newUser = authService.signUp(request.firstName(), request.lastName(), request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(CreateUserResponse.fromEntity(newUser));
    }

    @GetMapping("/me")
    public ResponseEntity<GetUserResponse> getCurrentUser(@AuthenticationPrincipal JWTUserData tokenData) {
        User user = authService.findUserByEmail(tokenData.email());
        return ResponseEntity.ok(GetUserResponse.fromEntity(user));
    }
}
