package org.example.service;

import org.example.audit.AuditLog;
import org.example.audit.UserAction;
import org.example.dto.CounterReadingDTO;
import org.example.mapper.MapperCR;
import org.example.model.CounterReading;
import org.example.model.User;
import org.example.repository.CounterReadingRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Обработчик методов показателя счетчика, также аудит на каждом действии
 */
public class CounterReadingService {
    /**
     * Поля инициализации
     */
    private CounterReadingRepository counterReadingRepository;
    private AuditLog auditLog;


    /**
     * Во время инстанса класса также инициализируется аудит и репозиторий показателей счетчика
     */
    public CounterReadingService() {
        this.auditLog = AuditLog.getInstance();
        this.counterReadingRepository = CounterReadingRepository.getInstance();

    }

    /**
     * Валидация вводимых показаний. Ни одно показание за этот месяц не должно быть меньше показаний предыдущего
     *
     * @return CounterReading если ошибок валидации нет; null - если ошибка валидации
     */
    public CounterReadingDTO validationCounter(User currentUser, CounterReadingDTO counterReading) {
        int id = currentUser.getId();
        var latestCounter = counterReadingRepository.findLastCounterReading(id);
        var latestCounterDTO = MapperCR.toDTO(latestCounter);
        if (latestCounter != null) {
            if (!counterReading.compare(latestCounterDTO)) {
                UserAction userAction = new UserAction(currentUser.getUsername(), "Error of validation", LocalDateTime.now());
                auditLog.logAction(userAction);
                return null;
            } else {
                return counterReading;
            }
        } else {
            return counterReading;
        }
    }

    public Map<String, Double> getTypeOfCounter() {
        List<String> data = counterReadingRepository.uniqueType(1);
        return data.stream()
                .collect(Collectors.toMap(
                        key -> key,
                        value -> null
                ));
    }

    /**
     * Обработчик внесения данных аутентифицированного пользователя
     *
     * @return CounterReading, если все нормально; null - если данные в этот месяц уже вносились
     */
    public CounterReadingDTO submitCounterReading(User currentUser, CounterReadingDTO counterReadingDTO) {
        int id = currentUser.getId();
        var counterList = counterReadingRepository.findAllByUserId(id);
        for (var counter : counterList) {
            if (counter.getMonth() == counterReadingDTO.getMonth() && counter.getYear() == counterReadingDTO.getYear()) {
                UserAction userAction = new UserAction(currentUser.getUsername(), "Submit Counter Reading failed. Data already exist.", LocalDateTime.now());
                auditLog.logAction(userAction);
                return null;
            }
        }
        var counterReading = MapperCR.toEntity(counterReadingDTO);
        counterReadingRepository.submit(counterReading);
        UserAction userAction = new UserAction(currentUser.getUsername(), "Submit Counter Reading success.", LocalDateTime.now());
        auditLog.logAction(userAction);
        return counterReadingDTO;
    }

    /**
     * Обработчик получения последних внесенных данных аутентифицированного пользователя
     *
     * @return CounterReading если все нормально; null если данные не найдены
     */
    public CounterReadingDTO getLatestCounterReading(User currentUser) {
        int id = currentUser.getId();
        var lastCountingReading = counterReadingRepository.findLastCounterReading(id);
        if (!lastCountingReading.isEmpty()) {
            UserAction userAction = new UserAction(currentUser.getUsername(), "Get Latest Counter Reading success", LocalDateTime.now());
            auditLog.logAction(userAction);
            return MapperCR.toDTO(lastCountingReading);
        } else {
            UserAction userAction = new UserAction(currentUser.getUsername(), "Get Latest Counter Reading failed. Data not found", LocalDateTime.now());
            auditLog.logAction(userAction);
            return null;
        }
    }

    /**
     * Обработчик получения данных за последний месяц аутентифицированного пользователя
     *
     * @return CounterReading если все нормально; null если данные не найдены
     */
    public CounterReadingDTO getCounterReadingForMonth(User currentUser, int month, int year) {
        int id = currentUser.getId();
        var counterReadingForMonth = counterReadingRepository.findCounterReadingForMonth(id, month, year);
        if (!counterReadingForMonth.isEmpty()) {
            UserAction userAction = new UserAction(currentUser.getUsername(), "Get Counter Reading For Month success", LocalDateTime.now());
            auditLog.logAction(userAction);
            return MapperCR.toDTO(counterReadingForMonth);
        } else {
            UserAction userAction = new UserAction(currentUser.getUsername(), "Get Counter Reading For Month failed. Data not found", LocalDateTime.now());
            auditLog.logAction(userAction);
            return null;
        }
    }

    /**
     * Обработчик получения истории вносимых данных аутентифицированного пользователя
     */
    public List<CounterReadingDTO> getAllCounterReadingForUser(User currentUser) {
        int id = currentUser.getId();
        var list = counterReadingRepository.findAllByUserId(id);
        UserAction userAction = new UserAction(currentUser.getUsername(), "Get All Counter Reading For Month", LocalDateTime.now());
        auditLog.logAction(userAction);
        var result = new ArrayList<CounterReadingDTO>();
        if (!list.isEmpty()) {
            var etalonYear = list.get(0).getYear();
            var etalonMonth = list.get(0).getMonth();
            var buf = new ArrayList<CounterReading>();
            for (var element : list) {
                if (element.getYear() == etalonYear && element.getMonth() == etalonMonth) {
                    buf.add(element);
                } else {
                    result.add(MapperCR.toDTO(buf));
                    buf.clear();
                    etalonYear = element.getYear();
                    etalonMonth = element.getMonth();
                    buf.add(element);
                }
            }
        }
        return result;
    }
}
