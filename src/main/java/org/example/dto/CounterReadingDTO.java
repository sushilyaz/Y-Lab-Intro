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
    // закрепление за пользователем
    private int userId;
    // год
    private int year;
    // месяц
    private int month;
    // мапа тип показания - значение
    private Map<String, Double> typeOfCounter;

    /**
     * Сравнение с значениями показаний за последний месяц
     */
    public boolean compare (CounterReadingDTO latest) {
        for (Map.Entry<String, Double> entry : latest.typeOfCounter.entrySet()) {
            if (entry.getValue() > this.typeOfCounter.get(entry.getKey()))
                return false;
        }
        return true;
    }

    /**
     * Для sout
     */
    @Override
    public String toString() {
        return "CounterReadingDTO{" +
                ", year=" + year +
                ", month=" + month +
                ", typeOfCounter=" + typeOfCounter +
                "}\n";
    }

    /**
     * Для сравнения
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CounterReadingDTO that = (CounterReadingDTO) o;
        return userId == that.userId && year == that.year && month == that.month && Objects.equals(typeOfCounter, that.typeOfCounter);
    }
    /**
     * По контракту если переопределяется equals, то и переопределяется hashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, year, month, typeOfCounter);
    }
}
