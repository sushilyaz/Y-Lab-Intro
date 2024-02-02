package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

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
}
