package org.example.service;

import org.example.model.CounterReading;
import org.example.model.User;
import org.example.repository.CounterReadingRepository;

public class CounterReadingService {
    private CounterReadingRepository counterReadingRepository;

    public CounterReadingService(CounterReadingRepository counterReadingRepository) {
        this.counterReadingRepository = counterReadingRepository;
    }

    public void submitCounterReading(User currentUser, CounterReading counterReading) {
        int id = currentUser.getId();
        var counterList = counterReadingRepository.findAllByUserId(id);
        for (var counter : counterList) {
            if (counter.getMonth() == counterReading.getMonth() && counter.getYear() == counterReading.getYear()) {
                System.out.println("Error! Data for this month already exists.");
                return;
            }
        }
        counterReading.setUserId(id);
        counterReadingRepository.submit(counterReading);
        System.out.println("Data entered successfully!");
    }

    public void getLatestCounterReading(User currentUser) {
        int id = currentUser.getId();
        var lastCountingReading = counterReadingRepository.findLastCounterReading(id);
        if (lastCountingReading != null) {
            System.out.println(lastCountingReading);
        } else {
            System.out.println("Error! User " + currentUser.getUsername() + " has not data");
        }
    }

    public void getCounterReadingForMonth(User currentUser, int month, int year) {
        int id = currentUser.getId();
        if (month < 1 || month > 12) {
            System.out.println("Invalid month. Please provide a valid month (1-12).");
        }
        var counterReadingForMonth = counterReadingRepository.findCounterReadingForMonth(id, month, year);
        if (counterReadingForMonth != null) {
            System.out.println(counterReadingForMonth);
        } else {
            System.out.println("Error! User has not data for " + month + "." + year);
        }
    }

    public void getAllCounterReadingForUser(User currentUser) {
        int id = currentUser.getId();
        var list = counterReadingRepository.findAllByUserId(id);
        if (!list.isEmpty()) {
            for (var counter : list) {
                System.out.println(counter);
            }
        } else {
            System.out.println("Error! User " + currentUser.getUsername() + " has not data");
        }
    }
}
