package org.example.repository;

import org.example.model.CounterReading;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Репозиторий показаний. Данные хранятся в листе. Реализован паттерн синглтон, вроде как множественного
 *  * доступа к этому приложению не планируется, поэтому реализовал не потокобезопасный вариант
 */
public class CounterReadingRepository{

    /**
     * Для синглтона
     */
    private static CounterReadingRepository instance;

    /**
     *  Так называемая "БД"
     */
    private List<CounterReading> counterReadings = new ArrayList<>();

    /**
     * приватный конструктор для синглтона
     */
    private CounterReadingRepository() {
    }

    /**
     * Для синглтона
     */
    public static CounterReadingRepository getInstance() {
        if (instance == null) {
            instance = new CounterReadingRepository();
        }
        return instance;
    }

    /**
     * Для тестирования (откатить "бд")
     */
    public static void reset() {
        instance = null;
    }

    /**
     * Геттер листа
     */
    public List<CounterReading> getCounterReadings() {
        return counterReadings;
    }

    /**
     *  поиск всех данных по id пользователя
     */
    public List<CounterReading> findAllByUserId(int userId) {
        return counterReadings.stream()
                .filter(counterReading -> counterReading.getUserId() == userId)
                .toList();
    }

    /**
     *  Сохранение в лист показаний
     */
    public void submit(CounterReading counterReading) {
        counterReadings.add(counterReading);
    }


    /**
     * Одно из требований к Д\З: "Последние поданые показания считаются актуальными".
     * Если проецировать на жизненную, ситуацию, то правильнее будет возвращать последнее по дате,
     * но я на всякий случай оставил закомментированное решение согласно ТЗ
     */
    public CounterReading findLastCounterReading(int userId) {
        var list = findAllByUserId(userId);
//        CounterReading lastElement;
//        if (list.isEmpty()) {
//            return null;
//        } else {
//            lastElement = list.get(list.size()-1);
//            return lastElement;
//        }
        var sortedList = list.stream()
                .sorted(Comparator.comparingInt(CounterReading::getYear)
                        .thenComparingInt(CounterReading::getMonth))
                .toList();

        CounterReading lastElement = sortedList.stream()
                .reduce((first, second) -> second)
                .orElse(null);
        return lastElement;

    }

    /**
     *  Поиск данных по месяцу и году
     */
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
