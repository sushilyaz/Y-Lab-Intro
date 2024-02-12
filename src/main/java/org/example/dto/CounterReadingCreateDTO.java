package org.example.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.Map;

@Data
public class CounterReadingCreateDTO {
    private int year;
    @Min(value = 1)
    @Max(value = 12)
    private int month;
    private Map<String, Double> typeOfCounter;
}
