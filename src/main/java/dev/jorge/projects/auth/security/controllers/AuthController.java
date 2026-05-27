package dev.jorge.projects.auth.security.controllers;

import dev.jorge.projects.auth.security.dtos.requests.LoginRequest;
import dev.jorge.projects.auth.security.dtos.responses.LoginResponse;

import dev.jorge.projects.auth.security.dtos.requests.RegisterUserRequest;
import dev.jorge.projects.auth.security.dtos.responses.RegisterUserResponse;
import dev.jorge.projects.auth.user.entities.User;
import dev.jorge.projects.auth.security.services.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        String token = authService.signIn(request.email(), request.password());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/api/v1/auth/register")
    public ResponseEntity<RegisterUserResponse> register(@RequestBody @Valid RegisterUserRequest request){
        User newUser = authService.signUp(request.firstName(), request.lastName(), request.username(),request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(RegisterUserResponse.fromEntity(newUser));
    }

}
