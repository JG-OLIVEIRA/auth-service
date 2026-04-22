package dev.jorge.projects.auth.api.service;

import dev.jorge.projects.auth.api.config.TokenConfig;
import dev.jorge.projects.auth.api.entity.User;
import dev.jorge.projects.auth.api.enums.Role;
import dev.jorge.projects.auth.api.exception.UserAlreadyExistsException;
import dev.jorge.projects.auth.api.exception.UserNotFoundException;
import dev.jorge.projects.auth.api.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenConfig tokenConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }

    public String signIn(String email, String password) {
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(userAndPass);

        User user = (User) authentication.getPrincipal();

        return tokenConfig.generateToken(user);
    }

    public User signUp(String firstName, String lastName, String email, String password) throws UserAlreadyExistsException {

        if(userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(email);
        }

        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setPassword(hashPassword(password));
        newUser.setRoles(Set.of(Role.ROLE_USER));

        return userRepository.save(newUser);
    }

    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }
}