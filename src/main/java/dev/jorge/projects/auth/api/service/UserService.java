package dev.jorge.projects.auth.api.service;

import dev.jorge.projects.auth.api.entity.User;
import dev.jorge.projects.auth.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
