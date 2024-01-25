package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.util.CurrentUser;

import java.util.Optional;

public class UserService {
    private UserRepository userRepository;
    private CurrentUser currentUser;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(String username, String password) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            System.out.println("User with username " + username + " already exist.");
        } else {
            User newUser = new User(username, password);
            userRepository.save(newUser);
            System.out.println("User with username" + username + " registred successfully !");
        }
    }

    public void loginUser(String username, String password) {
        Optional<User> userLogin = userRepository.findByUsername(username);
        if (userLogin.isPresent()) {
            User user = userLogin.get();
            if (user.getPassword().equals(password)) {
                user.setAuthenticated(true);
                currentUser.setCurrentUser(user);
                System.out.println("User with username " + username + " logging successfully!");
            } else {
                System.out.println("Incorrect password");
            }
        } else {
            System.out.println("User with username " + username + " not found");
        }
    }
}
