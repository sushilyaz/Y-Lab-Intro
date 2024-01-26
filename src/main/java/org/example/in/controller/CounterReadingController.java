package org.example.in.controller;


import org.example.model.CounterReading;
import org.example.model.TypeOfCounter;
import org.example.model.User;
import org.example.repository.CounterReadingRepository;
import org.example.repository.UserRepository;
import org.example.service.CounterReadingService;
import org.example.service.UserService;

import java.util.Scanner;

public class CounterReadingController {
    private CounterReadingRepository counterReadingRepository;

    private CounterReadingService counterReadingService;

    public CounterReadingController(CounterReadingRepository counterReadingRepository) {
        this.counterReadingRepository = counterReadingRepository;
        counterReadingService = new CounterReadingService(counterReadingRepository);
    }

    public void getLatestData(User currentUser) {
        var lastCountingReading = counterReadingService.getLatestCounterReading(currentUser);
        if (lastCountingReading != null) {
            System.out.println(lastCountingReading);
        } else {
            System.out.println("Error! User '" + currentUser.getUsername() + "' has not data");
        }
    }

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
        var counter = counterReadingService.submitCounterReading(currentUser, counterReading);
        if (counter != null) {
            System.out.println("Data entered successfully!");
        } else {
            System.out.println("Error! Data for this month already exists.");
        }
    }

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
