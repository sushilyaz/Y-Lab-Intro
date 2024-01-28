package org.example.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Модель показаний счетчика.
 * Зависимость от User по userId
 * Поле TypeOfCounter для последующего расширения, если необходимо добавить еще какие-то значения
 */
public class CounterReading {
    private int userId;
    private int year;
    private int month;
    private static Map<String, Double> commonTypeOfCounter = new HashMap<>() {{
        put("Cold water", null);
        put("Hot water", null);
        put("Heating", null);
    }};

    private Map<String, Double> typeOfCounter;


    public CounterReading(int year, int month, Map<String, Double> typeOfCounter) {
        this.year = year;
        this.month = month;
        this.typeOfCounter = new HashMap<>(typeOfCounter);
    }

    public Map<String, Double> getTypeOfCounter() {
        return typeOfCounter;
    }

    public void setTypeOfCounter(Map<String, Double> typeOfCounter) {
        this.typeOfCounter = typeOfCounter;
        commonTypeOfCounter.putAll(typeOfCounter);
    }

    // Метод для добавления нового ключа в рантайме
    public static void addNewKey(String key) {
        commonTypeOfCounter.put(key, null);
    }

    public static Map<String, Double> getCommonTypeOfCounter() {
        return commonTypeOfCounter;
    }

    public static void setCommonTypeOfCounter(Map<String, Double> commonTypeOfCounter) {
        CounterReading.commonTypeOfCounter = commonTypeOfCounter;
    }

    public boolean compare(CounterReading latestCounterReading) {
        for (Map.Entry<String, Double> entry : latestCounterReading.typeOfCounter.entrySet()) {
            if (entry.getValue() > this.typeOfCounter.get(entry.getKey()))
                return false;
        }
        return true;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    public String toString() {
        return "CounterReading{" +
                "year=" + year +
                ", month=" + month +
                ", typeOfCounter=" + typeOfCounter +
                "}\n";
    }

    /**
     * for tests
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CounterReading that = (CounterReading) o;
        return year == that.year && month == that.month && Objects.equals(typeOfCounter, that.typeOfCounter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, typeOfCounter);
    }
}
