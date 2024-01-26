package org.example.in.controller;


import org.example.model.CounterReading;
import org.example.model.TypeOfCounter;
import org.example.model.User;
import org.example.service.CounterReadingService;

import java.util.Scanner;

public class CounterReadingController {

    private CounterReadingService counterReadingService;

    public CounterReadingController() {
        counterReadingService = new CounterReadingService();
    }

    /**
     * Контроллер получения показателей аутентифицированного пользователя
     */
    public void getLatestData(User currentUser) {
        var lastCountingReading = counterReadingService.getLatestCounterReading(currentUser);
        if (lastCountingReading != null) {
            System.out.println(lastCountingReading);
        } else {
            System.out.println("Error! User '" + currentUser.getUsername() + "' has not data");
        }
    }
    /**
     * Контроллер внесения показателей аутентифицированного пользователя
     */
    public void putData(User currentUser) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter month: ");
        int month = scanner.nextInt();
        System.out.print("Enter year: ");
        int year = scanner.nextInt();
        System.out.print("Enter readings for Hot Water: ");
        double hw = scanner.nextDouble();
        System.out.print("Enter readings for Cold Water: ");
        double cw = scanner.nextDouble();
        System.out.print("Enter readings for Heating: ");
        double heating = scanner.nextDouble();
        TypeOfCounter typeOfCounter = new TypeOfCounter(hw, cw, heating);
        CounterReading counterReading = new CounterReading(year, month, typeOfCounter);
        var validCounterReading = counterReadingService.validationCounter(currentUser, counterReading);
        if (validCounterReading != null) {
            var counter = counterReadingService.submitCounterReading(currentUser, validCounterReading);
            if (counter != null) {
                System.out.println("Data entered successfully!");
            } else {
                System.out.println("Error! Data for this month already exists.");
            }
        } else {
            System.out.println("Data no valid. Latest counter cant be less than current");
        }
    }
    /**
     * Контроллер получения последних внесенных показателей аутентифицированного пользователя
     */
    public void getDataForMonth(User currentUser) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter month: ");
        int month = scanner.nextInt();
        if (month < 1 || month > 12) {
            System.out.println("Invalid month. Please provide a valid month (1-12).");
            getDataForMonth(currentUser);
        }
        System.out.print("Enter year:");
        int year = scanner.nextInt();
        var counterReadingForMonth = counterReadingService.getCounterReadingForMonth(currentUser, month, year);
        if (counterReadingForMonth != null) {
            System.out.println(counterReadingForMonth);
        } else {
            System.out.println("Error! User has not data for " + month + "." + year);
        }
    }

    /**
     * Контроллер получения внесенных за все время показателей аутентифицированного пользователя
     */
    public void getAllData(User currentUser) {
        var listOfCR = counterReadingService.getAllCounterReadingForUser(currentUser);
        System.out.println();
        if (listOfCR.isEmpty()) {
            System.out.println("User has no data");
        } else {
            System.out.println("User with username '" + currentUser.getUsername() + "' has data: ");
            for (var counter : listOfCR) {
                System.out.println(counter);
            }
            System.out.println();
        }
    }
}
