package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;

import java.util.Optional;

public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    private static int id = 1;

    public void registerUser(String username, String password) {
        Optional<User> existUser = userRepository.findByUsername(username);
        if (existUser.isPresent()) {
            System.out.println("User with username  " + username + " already exist");
        } else {
            id = userRepository.findLastId() + 1;
            User newUser = new User(id, username, password, false);
            userRepository.save(newUser);
            System.out.println("User with username " + username + " registred successfully!");
        }
    }

    public User authenticationUser (String username, String password) {
        Optional<User> existUser = userRepository.findByUsername(username);
        if (existUser.isPresent()) {
            User user = existUser.get();
            if (user.getPassword().equals(password)) {
                System.out.println("User with username " + username + " logging successfully!");
                return user;
            } else {
                System.out.println("Incorrect password");
                return null;
            }
        } else {
            System.out.println("User with username " + username + " is not registred!");
            return null;
        }
    }
}
