package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Этот класс необходим для преобразования выборки из нескольких строк в одну запись
 */
@Getter
@Setter
public class CounterReadingDTO {
    private LocalDate date;
    private String type;
    private Double value;
}
