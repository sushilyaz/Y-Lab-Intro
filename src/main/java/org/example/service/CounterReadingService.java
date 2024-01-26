package org.example.service;

import org.example.model.CounterReading;
import org.example.model.User;
import org.example.repository.CounterReadingRepository;

import java.util.List;

public class CounterReadingService {
    private CounterReadingRepository counterReadingRepository;


    public CounterReadingService() {;
        if (this.counterReadingRepository == null) {
            this.counterReadingRepository = CounterReadingRepository.getInstance();
        }
    }

    public CounterReading submitCounterReading(User currentUser, CounterReading counterReading) {
        int id = currentUser.getId();
        var counterList = counterReadingRepository.findAllByUserId(id);
        for (var counter : counterList) {
            if (counter.getMonth() == counterReading.getMonth() && counter.getYear() == counterReading.getYear()) {
                return null;
            }
        }
        counterReading.setUserId(id);
        counterReadingRepository.submit(counterReading);
        return counterReading;
    }

    public CounterReading getLatestCounterReading(User currentUser) {
        int id = currentUser.getId();
        var lastCountingReading = counterReadingRepository.findLastCounterReading(id);
        if (lastCountingReading != null) {
            return lastCountingReading;
        } else {
            return null;
        }
    }

    public CounterReading getCounterReadingForMonth(User currentUser, int month, int year) {
        int id = currentUser.getId();
        var counterReadingForMonth = counterReadingRepository.findCounterReadingForMonth(id, month, year);
        if (counterReadingForMonth != null) {
            return counterReadingForMonth;
        } else {
            return null;
        }
    }

    public List<CounterReading> getAllCounterReadingForUser(User currentUser) {
        int id = currentUser.getId();
        var list = counterReadingRepository.findAllByUserId(id);
        return list;
    }
}
