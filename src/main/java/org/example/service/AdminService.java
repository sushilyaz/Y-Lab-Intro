package org.example.service;

import org.example.dto.CounterReadingDTO;
import org.example.dto.UserInfoDTO;
import org.example.model.UserAction;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public interface AdminService {
    List<CounterReadingDTO> getCRByUser(String username);
    List<CounterReadingDTO> getLastUserInfo(String username);
    List<CounterReadingDTO> getUserInfoForMonth(String username, LocalDate date);
    List<String> getAllKey();
    boolean addNewKey(String newKey);
    List<UserAction> getLogs();
    List<UserInfoDTO> getAllUserInfo();
}
