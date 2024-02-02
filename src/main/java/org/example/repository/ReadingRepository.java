package org.example.repository;

import org.example.model.CounterReading;

import java.util.List;

public interface ReadingRepository {
    List<CounterReading> findAllByUserId(int userId);
    void save(List<CounterReading> counterReadings);
    List<CounterReading> findLastCounterReading(int userId);
    List<CounterReading> findCounterReadingForMonth(int userId, int month, int year);
}
