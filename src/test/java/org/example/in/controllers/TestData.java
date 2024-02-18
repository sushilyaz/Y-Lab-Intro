package org.example.in.controllers;

import org.example.dto.CounterReadingCreateDTO;
import org.example.dto.CounterReadingDTO;
import org.example.model.CounterReading;
import org.example.model.Role;
import org.example.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestData {
    List<CounterReadingCreateDTO> initCreateDTO() {
        List<CounterReadingCreateDTO> list = new ArrayList<>();
        CounterReadingCreateDTO dto1 = new CounterReadingCreateDTO();
        dto1.setDate(LocalDate.parse("2024-03-01"));
        dto1.setType("Cold Water");
        dto1.setValue(400.0);
        list.add(dto1);
        CounterReadingCreateDTO dto2 = new CounterReadingCreateDTO();
        dto2.setDate(LocalDate.parse("2024-03-01"));
        dto2.setType("Hot Water");
        dto2.setValue(400.0);
        list.add(dto2);
        CounterReadingCreateDTO dto3 = new CounterReadingCreateDTO();
        dto3.setDate(LocalDate.parse("2024-03-01"));
        dto3.setType("Heating");
        dto3.setValue(400.0);
        list.add(dto3);
        return list;
    }

    public List<CounterReadingDTO> initDTO() {
        List<CounterReadingDTO> list = new ArrayList<>();
        CounterReadingDTO dto1 = new CounterReadingDTO();
        dto1.setDate(LocalDate.parse("2024-03-01"));
        dto1.setType("Cold Water");
        dto1.setValue(400.0);
        list.add(dto1);
        CounterReadingDTO dto2 = new CounterReadingDTO();
        dto2.setDate(LocalDate.parse("2024-03-01"));
        dto2.setType("Hot Water");
        dto2.setValue(400.0);
        list.add(dto2);
        CounterReadingDTO dto3 = new CounterReadingDTO();
        dto3.setDate(LocalDate.parse("2024-03-01"));
        dto3.setType("Heating");
        dto3.setValue(400.0);
        list.add(dto3);
        return list;
    }

    User initTestUser() {
        User testUser = new User();
        testUser.setId(2L);
        testUser.setUsername("user1");
        testUser.setPassword("user1");
        testUser.setRole(Role.SIMPLE_USER);
        return testUser;
    }

    public List<CounterReading> testCR() {
        List<CounterReading> list = new ArrayList<>();
        CounterReading dto1 = new CounterReading();
        dto1.setDate(LocalDate.parse("2024-03-01"));
        dto1.setType("Cold Water");
        dto1.setValue(400.0);
        dto1.setUserId(2L);
        list.add(dto1);
        CounterReading dto2 = new CounterReading();
        dto2.setDate(LocalDate.parse("2024-03-01"));
        dto2.setType("Hot Water");
        dto2.setValue(400.0);
        dto2.setUserId(2L);
        list.add(dto2);
        CounterReading dto3 = new CounterReading();
        dto3.setDate(LocalDate.parse("2024-03-01"));
        dto3.setType("Heating");
        dto3.setValue(400.0);
        dto3.setUserId(2L);
        list.add(dto3);
        return list;
    }
}
