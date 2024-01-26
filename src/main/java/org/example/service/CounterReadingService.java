package org.example.service;

import org.example.audit.AuditLog;
import org.example.audit.UserAction;
import org.example.model.CounterReading;
import org.example.model.User;
import org.example.repository.CounterReadingRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Обработчик методов показателя счетчика, также аудит на каждом действии
 */
public class CounterReadingService {
    private CounterReadingRepository counterReadingRepository;
    private AuditLog auditLog;


    /**
     * Во время инстанса класса также инициализируется аудит и репозиторий показателей счетчика
     */
    public CounterReadingService() {
        this.auditLog = AuditLog.getInstance();
        this.counterReadingRepository = CounterReadingRepository.getInstance();

    }
    /**
     * Валидация вводимых показаний. Ни одно показание за этот месяц не должно быть меньше показаний предыдущего
     */
    public CounterReading validationCounter(User currentUser, CounterReading counterReading) {
        int id = currentUser.getId();
        var latestCounter = counterReadingRepository.findLastCounterReading(id);
            if (!counterReading.compare(latestCounter)){
                UserAction userAction = new UserAction(currentUser.getUsername(), "Error of validation", LocalDateTime.now());
                auditLog.logAction(userAction);
                return null;
            } else {
                return counterReading;
            }
    }

    /**
     * Обработчик внесения данных аутентифицированного пользователя
     */
    public CounterReading submitCounterReading(User currentUser, CounterReading counterReading) {
        int id = currentUser.getId();
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

    /**
     * Обработчик получения последних внесенных данных аутентифицированного пользователя
     */
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

    /**
     * Обработчик получения данных за последний месяц аутентифицированного пользователя
     */
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

    /**
     * Обработчик получения истории вносимых данных аутентифицированного пользователя
     */
    public List<CounterReading> getAllCounterReadingForUser(User currentUser) {
        int id = currentUser.getId();
        var list = counterReadingRepository.findAllByUserId(id);
        UserAction userAction = new UserAction(currentUser.getUsername(), "Get All Counter Reading For Month", LocalDateTime.now());
        auditLog.logAction(userAction);
        return list;
    }
}
