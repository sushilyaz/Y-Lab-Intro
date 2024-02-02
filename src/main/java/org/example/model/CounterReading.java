package org.example.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Модель показаний счетчика.
 */
public class CounterReading {
    private int id;

    /**
     * Поле зависимости от userId
     */
    private int userId;
    /**
     * Поле года вносимых показаний
     */
    private int year;
    /**
     * Поле месяца вносимых показаний
     */
    private int month;
    /**
     * Поле показаний. Это static поле необходимо для того, если админ в рантайме добавил новые виды показаний,
     * у обычного пользователя "обновился" список типов показаний.
     * Также это поле необходимо для инициализации начальных значений (применяется в CounterReadingController.putData)
     */
    private String type;
    private double value;


    /**
     * Конструктор
     */
    public CounterReading(int id, int userId, int year, int month, String type, double value) {
        this.id = id;
        this.userId = userId;
        this.year = year;
        this.month = month;
        this.type = type;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * геттер поля типов показаний
     */

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Метод для добавления ключа в рантайме
     */
    public static void addNewKey(String key) {
        commonTypeOfCounter.put(key, null);
    }

    /**
     * Сравнение всех значений ключей, который есть на данный момент в рантайме
     */
    public boolean compare(CounterReading latestCounterReading) {
        for (Map.Entry<String, Double> entry : latestCounterReading.typeOfCounter.entrySet()) {
            if (entry.getValue() > this.typeOfCounter.get(entry.getKey()))
                return false;
        }
        return true;
    }

    /**
     * геттеры и сеттеры
     */
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
                "userId=" + userId +
                ", year=" + year +
                ", month=" + month +
                ", type='" + type + '\'' +
                ", value=" + value +
                '}';
    }

    /**
     * Для sout
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CounterReading that = (CounterReading) o;
        return userId == that.userId && year == that.year && month == that.month && Double.compare(value, that.value) == 0 && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, year, month, type, value);
    }
}
