package org.example.repository;

import org.example.model.CounterReading;

import java.time.LocalDate;
import java.util.List;

public interface CounterReadingRepository {
    List<CounterReading> findAllByUserId(Long userId);
    void save(List<CounterReading> counterReadings);
    List<CounterReading> findLastCounterReading(Long userId);
    List<CounterReading> findCounterReadingForMonth(Long userId, LocalDate date);
    List<String> uniqueType(Long userId);
}
