package org.example.dto.adminDTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ForMonthDTO {
    private String username;
    @Min(value = 1)
    @Max(value = 12)
    private int month;
    private int year;
}
