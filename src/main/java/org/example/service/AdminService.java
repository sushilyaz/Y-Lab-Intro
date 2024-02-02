package org.example.service;

import org.example.audit.AuditLog;
import org.example.audit.UserAction;
import org.example.dto.CounterReadingDTO;
import org.example.dto.UserInfoDTO;
import org.example.mapper.MapperCR;
import org.example.repository.AdminRepository;
import org.example.repository.CounterReadingRepository;
import org.example.repository.UserRepository;

import java.util.List;

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
    private AdminRepository adminRepository;

    /**
     * Конструктор
     */
    public AdminService() {
        this.userRepository = UserRepository.getInstance();
        this.counterReadingRepository = CounterReadingRepository.getInstance();
        this.adminRepository = AdminRepository.getInstance();
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
    public CounterReadingDTO getLastUserInfo(String username) {
        var user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            var result = counterReadingRepository.findLastCounterReading(user.get().getId());
            return MapperCR.toDTO(result);
        } else {
            return null;
        }
    }

    /**
     * Обработчик получения показателей пользователя за конкретный месяц
     *
     * @return CounterReading если все ок; null если ошибки при обработке
     */
    public CounterReadingDTO getUserInfoForMonth(String username, int month, int year) {
        var user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            int id = user.get().getId();
            var counterReadingForMonth = counterReadingRepository.findCounterReadingForMonth(id, month, year);
            if (!counterReadingForMonth.isEmpty()) {
                return MapperCR.toDTO(counterReadingForMonth);
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
    public List<String> getAllKey() {
        return counterReadingRepository.uniqueType(1);
    }

    public boolean addNewKey(String newKey) {
        var list = counterReadingRepository.uniqueType(1);
        if (list.contains(newKey)) {
            return false;
        } else {
            counterReadingRepository.addNewType(newKey);
            return true;
        }
    }

    /**
     * Обработчик получения всех показателей всех пользователей.
     * Если информация найдена - возвращает заполненный лист. Если не найдена - пустой
     */
    public List<UserInfoDTO> getAllUserInfo() {
        return adminRepository.findUsersAndCR();
    }
}
