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
        System.out.print("Enter username (min 4 symbol): ");
        String username = scanner.nextLine();
        System.out.print("Enter password (min 4 symbol): ");
        String password = scanner.nextLine();
        if (username.length() < 4 || password.length() < 4) {
            System.out.println("\nValidation error");
            registration();
        }
        User user = userService.registerUser(username, password);
        if (user != null) {
            System.out.println("\nUser with username '" + username + "' registred successfully!");
        } else {
            System.out.println("\nUser with username '" + username + "' already exist");
        }
        Engine.start();
    }

    /**
     * Контроллер аутентификации обычного пользователя или админа и обработка результатов выполнения сервиса
     */
    public User autentification() {
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username (min 4 symbol): ");
        String authUsername = scanner.nextLine();
        System.out.print("Enter password (min 4 symbol): ");
        String authPassword = scanner.nextLine();
        User user = userService.authenticationUser(authUsername, authPassword);
        if (user != null && !user.getRoleAsString().equals("ADMIN")) {
            currentUser = user;
            System.out.println("\nAuthentification success!");
            return currentUser;
        } else if (user != null && user.getRoleAsString().equals("ADMIN")) {
            Engine.menuAdmin();
            System.out.println("\nAuthentification by Admin success!");
        } else {
            System.out.println("\nAuthentification is failed. Incorrect password or username. Try again");
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
