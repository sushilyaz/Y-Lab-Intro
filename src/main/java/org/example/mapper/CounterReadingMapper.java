package org.example.mapper;

import org.example.dto.CounterReadingDTO;
import org.example.model.CounterReading;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "default")
public interface CounterReadingMapper {
    CounterReadingMapper INSTANCE = Mappers.getMapper(CounterReadingMapper.class);
    default CounterReadingDTO map(List<CounterReading> counterReadings) {
        CounterReadingDTO dto = new CounterReadingDTO();
        dto.setUserId(counterReadings.get(0).getUserId());
        dto.setMonth(counterReadings.get(0).getMonth());
        dto.setYear(counterReadings.get(0).getYear());
        Map<String, Double> map = counterReadings.stream()
                        .collect(Collectors.toMap(CounterReading::getType, CounterReading::getValue));
        dto.setTypeOfCounter(map);
        return dto;
    }
}
