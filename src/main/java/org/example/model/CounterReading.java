package org.example.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Модель показаний счетчика.
 */
public class CounterReading {

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
    private static Map<String, Double> commonTypeOfCounter = new HashMap<>() {{
        put("Cold water", null);
        put("Hot water", null);
        put("Heating", null);
    }};

    /**
     * Поле показаний
     */
    private Map<String, Double> typeOfCounter;

    /**
     * Конструктор
     */
    public CounterReading(int year, int month, Map<String, Double> typeOfCounter) {
        this.year = year;
        this.month = month;
        this.typeOfCounter = new HashMap<>(typeOfCounter);
    }

    /**
     * геттер поля типов показаний
     */
    public Map<String, Double> getTypeOfCounter() {
        return typeOfCounter;
    }

    /**
     * Сеттер
     */
    public void setTypeOfCounter(Map<String, Double> typeOfCounter) {
        this.typeOfCounter = typeOfCounter;
        commonTypeOfCounter.putAll(typeOfCounter);
    }

    /**
     * Метод для добавления ключа в рантайме
     */
    public static void addNewKey(String key) {
        commonTypeOfCounter.put(key, null);
    }

    /**
     * геттер показаний для "обновления"
     */
    public static Map<String, Double> getCommonTypeOfCounter() {
        return commonTypeOfCounter;
    }

    /**
     * Сеттер
     */
    public static void setCommonTypeOfCounter(Map<String, Double> commonTypeOfCounter) {
        CounterReading.commonTypeOfCounter = commonTypeOfCounter;
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

    /**
     * Для sout
     */
    @Override
    public String toString() {
        return "CounterReading{" +
                "year=" + year +
                ", month=" + month +
                ", typeOfCounter=" + typeOfCounter +
                "}\n";
    }

    /**
     * Для тестирования
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
