package org.example.mapper;

import org.example.dto.CounterReadingCreateDTO;
import org.example.dto.CounterReadingDTO;
import org.example.model.CounterReading;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * маппер для CounterReading. Изначально я написал default методы, чтобы перевести из итерируемой структуры данных
 * в неитерированную (а мапстракт не может так). Я просто хотел, чтобы вывод был красивый. Ошибку понял, исправил
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Component
public interface CounterReadingMapper {
    CounterReadingMapper INSTANCE = Mappers.getMapper(CounterReadingMapper.class);

    @Mapping(source = "date", target = "date")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "value", target = "value")
    CounterReadingDTO entityToDTO(CounterReading model);
    List<CounterReadingDTO> entitiesToDTOs(List<CounterReading> models);
    @BeforeMapping
    default void beforeMapping(@MappingTarget CounterReading target, @Context Long userId) {
        target.setUserId(userId);
    }

    @Mapping(source = "date", target = "date")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "value", target = "value")
    CounterReading map(CounterReadingCreateDTO dto, @Context Long userId);

    @Mapping(target = "userId", source = "userId") // specify the mapping for userId
    List<CounterReading> map(List<CounterReadingCreateDTO> dtos, @Context Long userId);
}
