package org.example.in.controller;

import org.example.in.Engine;
import org.example.model.User;
import org.example.service.UserService;

import java.util.Scanner;

/**
 * Контроллер пользователя
 */
public class UserController {

    /**
     * Поле текущего пользователя (для реализации сессии
     */
    private User currentUser;

    /**
     * поле и инициализация сервиса
     */
    private UserService userService;

    public UserController() {
        userService = new UserService();
    }

    /**
     * Контроллер регистрации пользователя и обработка результатов выполнения сервиса
     */
    public void registration() {
        System.out.println();
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

    /**
     * Контроллер аутентификации обычного пользователя или админа и обработка результатов выполнения сервиса
     */
    public User autentification() {
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String authUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String authPassword = scanner.nextLine();
        User user = userService.authenticationUser(authUsername, authPassword);
        if (user != null && !user.getRoleAsString().equals("ADMIN")) {
            currentUser = user;
            System.out.println("Authentification success!");
            return currentUser;
        } else if (user != null && user.getRoleAsString().equals("ADMIN")) {
            Engine.menuAdmin();
            System.out.println("Authentification by Admin success!");
        } else {
            System.out.println("Authentification is failed. Incorrect password or username. Try again");
            Engine.start();
        }
        return null;
    }

    /**
     * Контроллер завершения сессии пользователя
     */
    public User logout(User user) {
        return userService.logoutUser(user);
    }
}
