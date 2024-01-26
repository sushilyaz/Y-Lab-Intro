package org.example;

import org.example.controller.AdminController;
import org.example.controller.CounterReadingController;
import org.example.controller.UserController;
import org.example.model.CounterReading;
import org.example.model.TypeOfCounter;
import org.example.model.User;

import java.util.Scanner;

public class Engine {
    private static UserController userController = new UserController();
    private static CounterReadingController controllerReadingController = new CounterReadingController();
    private static AdminController adminController = new AdminController();
    public static void start() {
        System.out.println("At first you need to register or log in. Choose an action");
        System.out.println("1 - Registration user (enter \"1\")");
        System.out.println("2 - Login user (enter \"2\")");
        System.out.println("3 - Exit (enter \"3\")");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter: ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                userController.registration();
                break;
            case 2:
                var currentUser = userController.autentification();
                menu(currentUser);
                break;

            case 3:
                System.out.println("Exit");
                System.exit(0);
                break;
        }
    }

    public static void menu(User currentUser) {
        System.out.println("Выберите действие: ");
        System.out.println("1 - Get actual counter readings");
        System.out.println("2 - Submit counter readings");
        System.out.println("3 - Get counter readings for specific month");
        System.out.println("4 - Get all counter readings");
        System.out.println("5 - Log out");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    controllerReadingController.getLatestData(currentUser);
                    menu(currentUser);
                    break;
                case 2:
                    controllerReadingController.putData(currentUser);
                    menu(currentUser);
                    break;
                case 3:
                    controllerReadingController.getDataForMonth(currentUser);
                    menu(currentUser);
                    break;
                case 4:
                    controllerReadingController.getAllData(currentUser);
                    menu(currentUser);
                    break;
                case 5:
                    currentUser = userController.logout();
                    start();
                    break;
            }
        }
    }

    public static void menuAdmin() {
        System.out.println("Выберите действие: ");
        System.out.println("1 - Get actual user counter readings");
        System.out.println("2 - Get user counter readings for specific month");
        System.out.println("3 - Get all counter readings users");
        System.out.println("4 - Log out");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    adminController.getActualCRUser();
                    menuAdmin();
                    break;
                case 2:
                    adminController.getCRUserForMonth();
                    menuAdmin();
                    break;
                case 3:
                    adminController.getAllInfo();
                    menuAdmin();
                    break;
                case 4:
                    start();
            }
        }
    }
}
