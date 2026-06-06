package dev.jorge.projects.auth.controller;

import dev.jorge.projects.auth.dto.request.LoginRequest;
import dev.jorge.projects.auth.dto.response.LoginResponse;

import dev.jorge.projects.auth.dto.request.RegisterUserRequest;
import dev.jorge.projects.auth.dto.response.RegisterUserResponse;
import dev.jorge.projects.auth.service.AuthService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/v1/auth/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        return authService.signIn(request);
    }

    @PostMapping("/api/v1/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterUserResponse register(@RequestBody @Valid RegisterUserRequest request){
        return authService.signUp(request);
    }

}
