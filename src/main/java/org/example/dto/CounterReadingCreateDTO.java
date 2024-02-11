package org.example.dto;

import lombok.Data;

import java.util.Map;

@Data
public class CounterReadingCreateDTO {
    private int year;
    private int month;
    private Map<String, Double> typeOfCounter;
}
