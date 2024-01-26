package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;

import java.util.Optional;

public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static int id = 1;

    public User registerUser(String username, String password) {
        Optional<User> existUser = userRepository.findByUsername(username);
        if (existUser.isPresent()) {
            return null;
        } else {
            id = userRepository.findLastId() + 1;
            User newUser = new User(id, username, password, false);
            userRepository.save(newUser);
            return newUser;
        }
    }

    public User authenticationUser (String username, String password) {
        Optional<User> existUser = userRepository.findByUsername(username);
        if (existUser.isPresent()) {
            User user = existUser.get();
            if (user.getPassword().equals(password)) {
                return user;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
