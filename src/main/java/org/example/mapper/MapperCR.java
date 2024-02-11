package org.example.mapper;

import org.example.dto.CounterReadingDTO;
import org.example.model.CounterReading;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapperCR {
    /**
     * Преобразование полученной выборки (select) в DTO
     */
    public static CounterReadingDTO toDTO(List<CounterReading> counterReadings) {

        int userId = counterReadings.get(0).getUserId();
        int year = counterReadings.get(0).getYear();
        int month = counterReadings.get(0).getMonth();

        Map<String, Double> typeOfCounter = counterReadings.stream()
                .collect(Collectors.toMap(
                        CounterReading::getType,
                        CounterReading::getValue
                ));

        CounterReadingDTO counterReadingDTO = new CounterReadingDTO(year, month, typeOfCounter);

        return counterReadingDTO;
    }
}
