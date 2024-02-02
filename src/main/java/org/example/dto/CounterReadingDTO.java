package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class CounterReadingDTO {
    private int userId;
    private int year;
    private int month;
    private Map<String, Double> typeOfCounter;

    public boolean compare (CounterReadingDTO latest) {
        for (Map.Entry<String, Double> entry : latest.typeOfCounter.entrySet()) {
            if (entry.getValue() > this.typeOfCounter.get(entry.getKey()))
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CounterReadingDTO{" +
                ", year=" + year +
                ", month=" + month +
                ", typeOfCounter=" + typeOfCounter +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CounterReadingDTO that = (CounterReadingDTO) o;
        return userId == that.userId && year == that.year && month == that.month && Objects.equals(typeOfCounter, that.typeOfCounter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, year, month, typeOfCounter);
    }
}
