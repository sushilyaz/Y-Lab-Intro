package org.example.repository;

import org.example.model.CounterReading;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CounterReadingRepository {
    private static List<CounterReading> counterReadings = new ArrayList<>();

    public List<CounterReading> getCounterReadings() {
        return counterReadings;
    }

    public List<CounterReading> findAllByUserId(int userId) {
        return counterReadings.stream()
                .filter(counterReading -> counterReading.getUserId() == userId)
                .toList();
    }

    public void submit(CounterReading counterReading) {
        counterReadings.add(counterReading);
    }


    /**
     * Одно из требований к Д\З: "Последние поданые показания считаются актуальными". Я сделал так, как написано,
     * как по мне логичнее было бы выводить за последний месяц, поэтому ниже закомментировал часть, которая сортирует
     * сначала по году, потом по месяцу и возвращает последнюю запись по времени.
     */
    public CounterReading findLastCounterReading(int userId) {
        var list = findAllByUserId(userId);
        CounterReading lastElement;
        if (list.isEmpty()) {
            return null;
        } else {
            lastElement = list.get(list.size()-1);
            return lastElement;
        }
//        var sortedList = list.stream()
//                .sorted(Comparator.comparingInt(CounterReading::getYear)
//                        .thenComparingInt(CounterReading::getMonth))
//                .toList();
//
//        CounterReading lastElement = sortedList.stream()
//                .reduce((first, second) -> second)
//                .orElse(null);


    }

    public CounterReading findCounterReadingForMonth(int userId, int month, int year) {
        var list = findAllByUserId(userId);
        for (var counter : list) {
            if (counter.getMonth() == month && counter.getYear() == year) {
                return counter;
            }
        }
        return null;
    }
}
