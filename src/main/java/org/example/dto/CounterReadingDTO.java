package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;

/**
 * Этот класс необходим для преобразования выборки из нескольких строк в одну запись
 */
@Getter
@Setter
@AllArgsConstructor
public class CounterReadingDTO {
    // год
    private int year;
    // месяц
    private int month;
    // мапа тип показания - значение
    private Map<String, Double> typeOfCounter;

    public CounterReadingDTO() {
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

    public Map<String, Double> getTypeOfCounter() {
        return typeOfCounter;
    }

    public void setTypeOfCounter(Map<String, Double> typeOfCounter) {
        this.typeOfCounter = typeOfCounter;
    }

    public boolean compare (CounterReadingDTO latest) {
        for (Map.Entry<String, Double> entry : latest.typeOfCounter.entrySet()) {
            if (entry.getValue() > this.typeOfCounter.get(entry.getKey()))
                return false;
        }
        if (this.month <= latest.getMonth() && this.year <= latest.getYear()) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Counter Reading for " +
                " year = " + year +
                " month = " + month +
                "have types and value of Counter: " + typeOfCounter +
                "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CounterReadingDTO that = (CounterReadingDTO) o;
        return year == that.year && month == that.month && Objects.equals(typeOfCounter, that.typeOfCounter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, typeOfCounter);
    }
}
