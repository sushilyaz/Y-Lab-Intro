package org.example.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class DateDTO {
    @Min(value = 1)
    @Max(value = 12)
    private int month;
    private int year;
}
