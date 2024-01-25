package org.example;

import org.example.model.CounterReading;
import org.example.model.TypeOfCounter;
import org.example.model.User;
import org.example.repository.CounterReadingRepository;
import org.example.repository.UserRepository;
import org.example.service.CounterReadingService;
import org.example.service.UserService;
import org.example.util.CurrentUser;

import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleApp {
    private static CurrentUser currentUser;
    private static UserService userService = new UserService(new UserRepository());
    private static CounterReadingService counterReadingService = new CounterReadingService(new CounterReadingRepository());

    public static void main(String[] args) {
        System.out.println("Welcome to housing and communal services !");
        start();
    }

    public static void start() {
        System.out.println("At first you need to register or log in. Choose an action");
        System.out.println("1 - Registration user (enter \"1\")");
        System.out.println("2 - Login user (enter \"2\")");
        System.out.println("3 - Exit (enter \"3\")");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                System.out.println("Enter username: ");
                String username = scanner.nextLine();
                System.out.println("Enter password: ");
                String password = scanner.nextLine();
                userService.registerUser(username, password);
                start();
                break;

            case 2:
                System.out.print("Enter username: ");
                String authUsername = scanner.nextLine();
                System.out.print("Enter password: ");
                String authPassword = scanner.nextLine();
                User user = userService.authenticationUser(authUsername, authPassword);
                if (user != null && !user.isAdmin()) {
                    currentUser = new CurrentUser(user);
                    menu();
                } else if (user != null && user.isAdmin()) {

                } else {
                    start();
                }
                break;

            case 3:
                System.out.println("Exit");
                System.exit(0);
                break;

        }
    }

    public static void menu() {
        System.out.println("Выберите действие: ");
        System.out.println("1 - Get current counter readings");
        System.out.println("2 - Submit counter readings");
        System.out.println("3 - Get counter readings for specific month");
        System.out.println("4 - Get all counter readings");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    counterReadingService.getLatestCounterReading(currentUser);
                    break;
                case 2:
                    System.out.println("Enter month:");
                    int month = scanner.nextInt();
                    System.out.println("Enter year:");
                    int year = scanner.nextInt();
                    System.out.println("Enter readings for Hot Water:");
                    double hw = scanner.nextDouble();
                    System.out.println("Enter readings for Cold Water:");
                    double cw = scanner.nextDouble();
                    System.out.println("Enter readings for Heating Water:");
                    double heating = scanner.nextDouble();
                    TypeOfCounter typeOfCounter = new TypeOfCounter(hw, cw, heating);
                    CounterReading counterReading = new CounterReading(year, month, typeOfCounter);
                    counterReadingService.submitCounterReading(currentUser, counterReading);
                    break;
                case 3:
                    System.out.println("Enter month:");
                    int monthCR = scanner.nextInt();
                    System.out.println("Enter year:");
                    int yearCR = scanner.nextInt();
                    counterReadingService.getCounterReadingForMonth(currentUser, monthCR, yearCR);
                    break;
                case 4:
                    counterReadingService.getAllCounterReadingForUser(currentUser);
                    break;
            }
        }
    }
}
