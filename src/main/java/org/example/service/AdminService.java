package org.example.service;

import org.example.audit.AuditLog;
import org.example.audit.UserAction;
import org.example.dto.UserInfoDTO;
import org.example.model.CounterReading;
import org.example.model.User;
import org.example.repository.CounterReadingRepository;
import org.example.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Сервис администратора, админ в журналировании не участвует. Его действия не фиксируются
 */
public class AdminService {

    /**
     * Поля аудита, и двух репозиториев, так как админу нужен доступ ко всему
     */
    private AuditLog auditLog;
    private UserRepository userRepository;
    private CounterReadingRepository counterReadingRepository;

    /**
     * Конструктор
     */
    public AdminService() {
        this.userRepository = UserRepository.getInstance();
        this.counterReadingRepository = CounterReadingRepository.getInstance();
        this.auditLog = AuditLog.getInstance();
    }

    /**
     * Обработчик получения логов
     */
    public List<UserAction> getLogs() {
        return auditLog.getUserActions();
    }

    /**
     * Обработчик получения последних внесенных показателей пользователя.
     *
     * @return CounterReading если все ок; null если ошибки при обработке
     */
    public CounterReading getLastUserInfo(String username) {
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

    /**
     * Обработчик получения показателей пользователя за конкретный месяц
     *
     * @return CounterReading если все ок; null если ошибки при обработке
     */
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

    /**
     * Получение всех типов показаний (только ключей)
     *
     * @return Set<String>
     */
    public Set<String> getAllKey() {
        return CounterReading.getCommonTypeOfCounter().keySet();
    }

    public boolean addNewKey(String newKey) {
        var map = CounterReading.getCommonTypeOfCounter();
        if (map.containsKey(newKey)) {
            return false;
        } else {
            CounterReading.addNewKey(newKey);
            return true;
        }
    }

    /**
     * Обработчик получения всех показателей всех пользователей.
     * Если информация найдена - возвращает заполненный лист. Если не найдена - пустой
     */
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
                                counterReading.getTypeOfCounter()
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
