package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CounterReadingCreateDTO {
    private LocalDate date;
    private String type;
    private Double value;
}
