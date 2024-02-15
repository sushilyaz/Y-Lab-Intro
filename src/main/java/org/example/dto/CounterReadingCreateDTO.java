package org.example.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CounterReadingCreateDTO {
    private LocalDate date;
    private String type;
    private Double value;
}
