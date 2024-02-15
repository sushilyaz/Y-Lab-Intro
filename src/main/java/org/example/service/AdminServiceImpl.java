package org.example.service;

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
import org.example.repository.UserActionRepository;
import org.example.repository.UserRepository;
import org.example.repository.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сервис администратора, админ в журналировании не участвует. Его действия не фиксируются
 */
@Component
public class AdminServiceImpl implements AdminService{

    /**
     * Поля аудита, и двух репозиториев, так как админу нужен доступ ко всему
     */
    private final UserActionRepository userActionRepository;
    private final UserRepository userRepository;
    private final CounterReadingRepository counterReadingRepository;
    private final AdminRepository adminRepository;

    /**
     * Конструктор
     */
    @Autowired
    public AdminServiceImpl(UserActionRepository userActionRepository, UserRepositoryImpl userRepository, CounterReadingRepositoryImpl counterReadingRepository, AdminRepositoryImpl adminRepository) {
        this.userActionRepository = userActionRepository;
        this.userRepository = userRepository;
        this.counterReadingRepository = counterReadingRepository;
        this.adminRepository = adminRepository;
    }

    /**
     * Обработчик получения последних внесенных показателей пользователя.
     */

    public List<CounterReadingDTO> getCRByUser(User currentUser) {
        var user = userRepository.findByUsername(currentUser.getUsername());
        if (user.isPresent()) {
            List<CounterReading> result = counterReadingRepository.findAllByUserId(user.get().getId());
            if (!result.isEmpty()) {
                return CounterReadingMapper.INSTANCE.entitiesToDTOs(result);
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
    public List<CounterReadingDTO> getLastUserInfo(User currentUser) {
        Optional<User> user = userRepository.findByUsername(currentUser.getUsername());
        if (user.isPresent()) {
            List<CounterReading> result = counterReadingRepository.findLastCounterReading(user.get().getId());
            if (!result.isEmpty()) {
                return CounterReadingMapper.INSTANCE.entitiesToDTOs(result);
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
    public List<CounterReadingDTO> getUserInfoForMonth(User currentUser, LocalDate date) {
        var user = userRepository.findByUsername(currentUser.getUsername());
        if (user.isPresent()) {
            Long id = user.get().getId();
            List<CounterReading> counterReadingForMonth = counterReadingRepository.findCounterReadingForMonth(id, date);
            if (!counterReadingForMonth.isEmpty()) {
                return CounterReadingMapper.INSTANCE.entitiesToDTOs(counterReadingForMonth);
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
    public List<String> getAllKey() {
        return counterReadingRepository.uniqueType(1L);
    }

    /**
     * Добавление новых типов показаний
     */
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
    public List<UserAction> getLogs() {
        return userActionRepository.getUserActions();
    }

    /**
     * Обработчик получения всех показателей всех пользователей.
     * Если информация найдена - возвращает заполненный лист. Если не найдена - пустой
     */
    public List<UserInfoDTO> getAllUserInfo() {
        return adminRepository.findUsersAndCR();
    }
}
