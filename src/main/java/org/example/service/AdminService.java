package org.example.service;

import org.example.dto.UserInfoDTO;
import org.example.model.CounterReading;
import org.example.model.User;
import org.example.repository.CounterReadingRepository;
import org.example.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class AdminService {
    private UserRepository userRepository;
    private CounterReadingRepository counterReadingRepository;

    public AdminService() {
        if (this.userRepository == null) {
            this.userRepository = UserRepository.getInstance();
        }
        if (this.counterReadingRepository == null) {
            this.counterReadingRepository = CounterReadingRepository.getInstance();
        }
    }

    public CounterReading getUserInfo(String username) {
        var user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            int id = user.get().getId();
            var lastCountingReading = counterReadingRepository.findLastCounterReading(id);
            if (lastCountingReading != null) {
                return lastCountingReading;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public CounterReading getUserInfoForMonth(String username, int month, int year) {
        var user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            int id = user.get().getId();
            var counterReadingForMonth = counterReadingRepository.findCounterReadingForMonth(id, month, year);
            if (counterReadingForMonth != null) {
                return counterReadingForMonth;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public List<UserInfoDTO> getAllUserInfo() {
        var users = userRepository.getUsers();
        var counters = counterReadingRepository.getCounterReadings();
        List<UserInfoDTO> userInfos = new ArrayList<>();
        if (!users.isEmpty() && !counters.isEmpty()) {
            for (User user : users) {
                for (CounterReading counterReading : counters) {
                    if (user.getId() == counterReading.getUserId()) {
                        UserInfoDTO userInfoDTO = new UserInfoDTO(
                                user.getUsername(),
                                counterReading.getYear(),
                                counterReading.getMonth(),
                                counterReading.getTypeOfCounter().getHotWater(),
                                counterReading.getTypeOfCounter().getColdWater(),
                                counterReading.getTypeOfCounter().getHeating()
                        );
                        userInfos.add(userInfoDTO);
                    }
                }
            }
            return userInfos;
        } else {
            return userInfos;
        }
    }
}
