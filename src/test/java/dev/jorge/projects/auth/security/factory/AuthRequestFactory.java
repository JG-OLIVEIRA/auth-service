package dev.jorge.projects.auth.security.factory;

import dev.jorge.projects.auth.security.dtos.requests.LoginRequest;
import dev.jorge.projects.auth.security.dtos.requests.RegisterUserRequest;

public class AuthRequestFactory {

    public static LoginRequest buildLoginRequest() {
        return new LoginRequest("jorge.gdoliveira8@gmail.com", "password");
    }

    public static RegisterUserRequest buildRegisterUserRequest() {
        return new RegisterUserRequest("Jorge", "Gonçalves de Oliveira", "jorge.gdoliveira8", "jorge.gdoliveira8@gmail.com","password");
    }
}
