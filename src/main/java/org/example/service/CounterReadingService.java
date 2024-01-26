package org.example.service;

import org.example.audit.AuditLog;
import org.example.audit.UserAction;
import org.example.model.CounterReading;
import org.example.model.User;
import org.example.repository.CounterReadingRepository;

import java.time.LocalDateTime;
import java.util.List;

public class CounterReadingService {
    private CounterReadingRepository counterReadingRepository;
    private AuditLog auditLog;


    public CounterReadingService() {
        this.auditLog = AuditLog.getInstance();
        this.counterReadingRepository = CounterReadingRepository.getInstance();

    }

    public CounterReading submitCounterReading(User currentUser, CounterReading counterReading) {
        int id = currentUser.getId();
        var latestCounter = counterReadingRepository.findLastCounterReading(id);

        var counterList = counterReadingRepository.findAllByUserId(id);
        for (var counter : counterList) {
            if (counter.getMonth() == counterReading.getMonth() && counter.getYear() == counterReading.getYear()) {
                UserAction userAction = new UserAction(currentUser.getUsername(), "Submit Counter Reading failed. Data already exist.", LocalDateTime.now());
                auditLog.logAction(userAction);
                return null;
            }
        }
        counterReading.setUserId(id);
        counterReadingRepository.submit(counterReading);
        UserAction userAction = new UserAction(currentUser.getUsername(), "Submit Counter Reading success.", LocalDateTime.now());
        auditLog.logAction(userAction);
        return counterReading;
    }

    public CounterReading getLatestCounterReading(User currentUser) {
        int id = currentUser.getId();
        var lastCountingReading = counterReadingRepository.findLastCounterReading(id);
        if (lastCountingReading != null) {
            UserAction userAction = new UserAction(currentUser.getUsername(), "Get Latest Counter Reading success", LocalDateTime.now());
            auditLog.logAction(userAction);
            return lastCountingReading;
        } else {
            UserAction userAction = new UserAction(currentUser.getUsername(), "Get Latest Counter Reading failed. Data not found", LocalDateTime.now());
            auditLog.logAction(userAction);
            return null;
        }
    }

    public CounterReading getCounterReadingForMonth(User currentUser, int month, int year) {
        int id = currentUser.getId();
        var counterReadingForMonth = counterReadingRepository.findCounterReadingForMonth(id, month, year);
        if (counterReadingForMonth != null) {
            UserAction userAction = new UserAction(currentUser.getUsername(), "Get Counter Reading For Month success", LocalDateTime.now());
            auditLog.logAction(userAction);
            return counterReadingForMonth;
        } else {
            UserAction userAction = new UserAction(currentUser.getUsername(), "Get Counter Reading For Month failed. Data not found", LocalDateTime.now());
            auditLog.logAction(userAction);
            return null;
        }
    }

    public List<CounterReading> getAllCounterReadingForUser(User currentUser) {
        int id = currentUser.getId();
        var list = counterReadingRepository.findAllByUserId(id);
        UserAction userAction = new UserAction(currentUser.getUsername(), "Get All Counter Reading For Month", LocalDateTime.now());
        auditLog.logAction(userAction);
        return list;
    }
}
