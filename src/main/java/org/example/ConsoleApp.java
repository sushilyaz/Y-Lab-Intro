package org.example;

import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.example.util.CurrentUser;

import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleApp {
    private static CurrentUser currentUser;
    private static UserService userService;

    public static void main(String[] args) {
        System.out.println("Welcome to housing and communal services !");
        start();
    }

    public static void start() {
        System.out.println("At first you need to register or log in. Choose an action");
        System.out.println("1. Registration user (enter \"1\")");
        System.out.println("2. Login user (enter \"2\")");
        System.out.println("3. Exit (enter \"3\")");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.print("Enter username: ");
                String username = scanner.next();
                System.out.print("Enter password: ");
                String password = scanner.next();
                userService.registerUser(username, password);
                break;

            case 2:
                System.out.print("Enter username: ");
                String authUsername = scanner.next();
                System.out.print("Enter password: ");
                String authPassword = scanner.next();
                currentUser = new CurrentUser(userService.authenticationUser(authUsername, authPassword));
                menu();
                break;

            case 3:
                System.out.println("Выход из программы.");
                System.exit(0);
                break;

        }
    }

    public static void menu() {

    }
}
