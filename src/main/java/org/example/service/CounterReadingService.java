package org.example.service;

import org.example.dto.CounterReadingCreateDTO;
import org.example.dto.CounterReadingDTO;
import org.example.model.User;

import java.time.LocalDate;
import java.util.List;

public interface CounterReadingService {
    List<CounterReadingCreateDTO> validationCounter(User currentUser, List<CounterReadingCreateDTO> counterReading);
    List<CounterReadingDTO> submitCounterReading(User currentUser, List<CounterReadingCreateDTO> dtoCreate);
    List<CounterReadingDTO> getLastUserInfo(User currentUser);
    List<CounterReadingDTO> getUserInfoForMonth(User currentUser, LocalDate date);
    List<CounterReadingDTO> getCRByUser(User currentUser);

}
