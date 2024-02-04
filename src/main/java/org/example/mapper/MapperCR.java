package org.example.mapper;

import org.example.dto.CounterReadingDTO;
import org.example.model.CounterReading;

import java.util.ArrayList;
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

        CounterReadingDTO counterReadingDTO = new CounterReadingDTO(userId, year, month, typeOfCounter);

        return counterReadingDTO;
    }

    /**
     * Из DTO обратно в сущность
     */
    public static List<CounterReading> toEntity(CounterReadingDTO counterReadingDTO) {
        List<CounterReading> counterReadings = new ArrayList<>();
        for (Map.Entry<String, Double> map : counterReadingDTO.getTypeOfCounter().entrySet()) {
            var counterReading = new CounterReading(1, counterReadingDTO.getUserId(), counterReadingDTO.getYear(), counterReadingDTO.getMonth(), map.getKey(), map.getValue());
            counterReadings.add(counterReading);
        }
        return counterReadings;
    }
}
