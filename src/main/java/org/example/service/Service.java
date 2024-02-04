package org.example.service;

import org.example.dto.CounterReadingDTO;
import org.example.model.User;

import java.util.List;

/**
 * Добавил интерфейс для сервисов
 */
public interface Service {
    List<CounterReadingDTO> getCRByUser(User currentUser);
    CounterReadingDTO getLastUserInfo(User currentUser);
    CounterReadingDTO getUserInfoForMonth(User currentUser, int month, int year);
}
