package org.example.mapper;

import org.example.dto.CounterReadingCreateDTO;
import org.example.dto.CounterReadingDTO;
import org.example.model.CounterReading;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "default")
public interface CounterReadingMapper {
    CounterReadingMapper INSTANCE = Mappers.getMapper(CounterReadingMapper.class);

    default CounterReadingDTO map(List<CounterReading> counterReadings) {
        CounterReadingDTO dto = new CounterReadingDTO();
        dto.setMonth(counterReadings.get(0).getMonth());
        dto.setYear(counterReadings.get(0).getYear());
        Map<String, Double> map = counterReadings.stream()
                .collect(Collectors.toMap(CounterReading::getType, CounterReading::getValue));
        dto.setTypeOfCounter(map);
        return dto;
    }

    default List<CounterReading> map(CounterReadingCreateDTO dto, int id) {
        List<CounterReading> entities = new ArrayList<>();
        entities.addAll(dto.getTypeOfCounter().entrySet().stream()
                .map(entry -> {
                    CounterReading counterReading = new CounterReading();
                    counterReading.setUserId(id);
                    counterReading.setYear(dto.getYear());
                    counterReading.setMonth(dto.getMonth());
                    counterReading.setType(entry.getKey());
                    counterReading.setValue(entry.getValue());
                    return counterReading;
                })
                .toList());
        return entities;
    }
    default CounterReadingDTO mapCreateToDto (CounterReadingCreateDTO createDTO) {
        return new CounterReadingDTO(createDTO.getYear(), createDTO.getMonth(), createDTO.getTypeOfCounter());
    }
    default List<CounterReadingDTO> toListDTO (List<CounterReading> counterReadings) {
        int startMonth = counterReadings.get(0).getMonth();
        int startYear = counterReadings.get(0).getYear();
        List<CounterReading> buf = new ArrayList<>();
        List<CounterReadingDTO> result = new ArrayList<>();
        int count = 0;
        for (var reading : counterReadings) {
            count++;
            if (reading.getMonth() == startMonth && reading.getYear() == startYear) {
                buf.add(reading);
            } else {
                result.add(map(buf));
                buf.clear();
                startMonth = reading.getMonth();
                startYear = reading.getYear();
                buf.add(reading);
            }
        }
        if (count == counterReadings.size()) {
            result.add(map(buf));
        }
        return result;
    }
}
