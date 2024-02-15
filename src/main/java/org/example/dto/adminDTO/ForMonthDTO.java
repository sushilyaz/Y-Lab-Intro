package org.example.dto.adminDTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ForMonthDTO {
    private String username;
    private LocalDate date;
}
