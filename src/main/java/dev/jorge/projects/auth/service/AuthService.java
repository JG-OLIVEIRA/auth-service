package dev.jorge.projects.auth.service;

import dev.jorge.projects.auth.config.TokenConfig;

import dev.jorge.projects.auth.dto.request.LoginRequest;
import dev.jorge.projects.auth.dto.request.RegisterUserRequest;
import dev.jorge.projects.auth.dto.response.LoginResponse;
import dev.jorge.projects.auth.dto.response.RegisterUserResponse;
import dev.jorge.projects.auth.producer.AuthProducer;
import dev.jorge.projects.auth.model.User;
import dev.jorge.projects.auth.enums.Role;
import dev.jorge.projects.auth.exception.UserAlreadyExistsException;
import dev.jorge.projects.auth.exception.UserNotFoundException;
import dev.jorge.projects.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;
    private final AuthProducer authProducer;

    @Transactional(readOnly = true)
    public LoginResponse signIn(LoginRequest request) {
        String email = request.email();
        String password = request.password();

        User user = findUserByEmail(email);
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(email, password);
        if(authenticationManager.authenticate(userAndPass).isAuthenticated()){
            String token = tokenConfig.generateSessionToken(user);
            return new LoginResponse(token);
        }
        throw new BadCredentialsException("Credenciais inválidas");
    }

    @Transactional
    public RegisterUserResponse signUp(RegisterUserRequest request) {
        String email = request.email();
        String username = request.username();

        if(userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(email);
        }

        if(userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException(username);
        }

        User newUser = new User();
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(hashPassword(request.password()));
        newUser.setRoles(Set.of(Role.ROLE_USER));

        userRepository.save(newUser);
        authProducer.publishMessageEmail(newUser);

        return new RegisterUserResponse(newUser.getFirstName(), newUser.getLastName(), newUser.getUsername(), newUser.getEmail());
    }

    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}