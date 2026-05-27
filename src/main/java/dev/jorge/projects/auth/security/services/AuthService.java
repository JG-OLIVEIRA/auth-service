package dev.jorge.projects.auth.security.services;

import dev.jorge.projects.auth.security.configs.TokenConfig;

import dev.jorge.projects.auth.security.producers.AuthProducer;
import dev.jorge.projects.auth.user.entities.User;
import dev.jorge.projects.auth.user.enums.Role;
import dev.jorge.projects.auth.user.exceptions.UserAlreadyExistsException;
import dev.jorge.projects.auth.user.exceptions.UserNotFoundException;
import dev.jorge.projects.auth.user.repositories.UserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;
    private final AuthProducer authProducer;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            TokenConfig tokenConfig,
            AuthProducer authProducer
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
        this.authProducer = authProducer;
    }

    @Transactional(readOnly = true)
    public String signIn(String email, String password) {
        User user = findUserByEmail(email);
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(email, password);
        if(authenticationManager.authenticate(userAndPass).isAuthenticated()){
            return tokenConfig.generateSessionToken(user);
        }
        throw new BadCredentialsException("Credenciais inválidas");
    }

    @Transactional
    public User signUp(String firstName, String lastName, String username, String email, String password) {

        if(userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(email);
        }

        if(userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException(username);
        }

        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(hashPassword(password));
        newUser.setRoles(Set.of(Role.ROLE_USER));

        userRepository.save(newUser);
        authProducer.publishMessageEmail(newUser);

        return newUser;
    }

    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}