package org.example.service;

import org.example.aspects.Audit;
import org.example.dto.CounterReadingDTO;
import org.example.dto.UserInfoDTO;
import org.example.mapper.CounterReadingMapper;
import org.example.model.CounterReading;
import org.example.model.User;
import org.example.model.UserAction;
import org.example.repository.AdminRepository;
import org.example.repository.AdminRepositoryImpl;
import org.example.repository.CounterReadingRepository;
import org.example.repository.CounterReadingRepositoryImpl;
import org.example.repository.UserActionRepositoryImpl;
import org.example.repository.UserRepository;
import org.example.repository.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сервис администратора, админ в журналировании не участвует. Его действия не фиксируются
 */
@Service
public class AdminServiceImpl implements AdminService{

    /**
     * Поля аудита, и двух репозиториев, так как админу нужен доступ ко всему
     */
    private final UserActionRepositoryImpl userActionRepository;
    private final UserRepository userRepository;
    private final CounterReadingRepository counterReadingRepository;
    private final AdminRepository adminRepository;
    private final CounterReadingMapper counterReadingMapper;

    /**
     * Конструктор
     */
    @Autowired
    public AdminServiceImpl(UserActionRepositoryImpl userActionRepository, UserRepositoryImpl userRepository, CounterReadingRepositoryImpl counterReadingRepository, AdminRepositoryImpl adminRepository, CounterReadingMapper counterReadingMapper) {
        this.userActionRepository = userActionRepository;
        this.userRepository = userRepository;
        this.counterReadingRepository = counterReadingRepository;
        this.adminRepository = adminRepository;
        this.counterReadingMapper = counterReadingMapper;
    }

    /**
     * Обработчик получения последних внесенных показателей пользователя.
     */
    @Audit
    public List<CounterReadingDTO> getCRByUser(String username) {
        var user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            List<CounterReading> result = counterReadingRepository.findAllByUserId(user.get().getId());
            if (!result.isEmpty()) {
                return counterReadingMapper.entitiesToDTOs(result);
            } else {
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Получение последних данных пользователя
     */
    @Audit
    public List<CounterReadingDTO> getLastUserInfo(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            List<CounterReading> result = counterReadingRepository.findLastCounterReading(user.get().getId());
            if (!result.isEmpty()) {
                return counterReadingMapper.entitiesToDTOs(result);
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
     * @return CounterReadingDTO если все ок; null если ошибки при обработке
     */
    @Audit
    public List<CounterReadingDTO> getUserInfoForMonth(String username, LocalDate date) {
        var user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            Long id = user.get().getId();
            List<CounterReading> counterReadingForMonth = counterReadingRepository.findCounterReadingForMonth(id, date);
            if (!counterReadingForMonth.isEmpty()) {
                return counterReadingMapper.entitiesToDTOs(counterReadingForMonth);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Получение всех типов показаний (только ключей)
     * Здесь идея в том, что новые показания добавляются к пользователю admin(администратор)
     * Он грубо говоря является неким центром, я посчитал, что так будет очень удобно, поэтому в uniqueType
     * передается параметр userId (1) - это администратор
     * @return List<String>
     */
    @Audit
    public List<String> getAllKey() {
        return counterReadingRepository.uniqueType(1L);
    }

    /**
     * Добавление новых типов показаний
     */
    @Audit
    public boolean addNewKey(String newKey) {
        List<String> list = counterReadingRepository.uniqueType(1L);
        if (list.contains(newKey)) {
            return false;
        } else {
            adminRepository.addNewType(newKey);
            return true;
        }
    }

    /**
     * Обработчик получения логов
     */
    @Audit
    public List<UserAction> getLogs() {
        return userActionRepository.getUserActions();
    }

    /**
     * Обработчик получения всех показателей всех пользователей.
     * Если информация найдена - возвращает заполненный лист. Если не найдена - пустой
     */
    @Audit
    public List<UserInfoDTO> getAllUserInfo() {
        return adminRepository.findUsersAndCR();
    }
}
