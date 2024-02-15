package org.example.service;

import org.example.dto.CounterReadingDTO;
import org.example.dto.UserInfoDTO;
import org.example.model.User;
import org.example.model.UserAction;

import java.time.LocalDate;
import java.util.List;

public interface AdminService {
    List<CounterReadingDTO> getCRByUser(User currentUser);
    List<CounterReadingDTO> getLastUserInfo(User currentUser);
    List<CounterReadingDTO> getUserInfoForMonth(User currentUser, LocalDate date);
    List<String> getAllKey();
    boolean addNewKey(String newKey);
    List<UserAction> getLogs();
    List<UserInfoDTO> getAllUserInfo();
}
