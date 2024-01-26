package org.example.controller;

import com.sun.tools.javac.Main;
import org.example.Engine;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;

import java.util.Scanner;

public class UserController {

    private User currentUser;
    private UserRepository userRepository = new UserRepository();
    private UserService userService = new UserService(userRepository);

    public void registration() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        User user = userService.registerUser(username, password);
        if (user != null) {
            System.out.println("User with username '" + username + "' registred successfully!");
        } else {
            System.out.println("User with username '" + username + "' already exist");
        }
        Engine.start();
    }

    public User autentification() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String authUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String authPassword = scanner.nextLine();
        User user = userService.authenticationUser(authUsername, authPassword);
        if (user != null && !user.isAdmin()) {
            currentUser = user;
            System.out.println("Authentification success!");
            return currentUser;
        } else if (user != null && user.isAdmin()) {
            Engine.menuAdmin();
            System.out.println("Authentification by Admin success!");
        } else {
            System.out.println("Authentification is failed. Incorrect password or username. Try again");
            Engine.start();
        }
        return null;
    }

    public User logout() {
        return null;
    }
}
