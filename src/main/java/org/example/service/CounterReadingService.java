package org.example.service;

import org.example.repository.UserActionRepository;
import org.example.model.UserAction;
import org.example.dto.CounterReadingDTO;
import org.example.mapper.MapperCR;
import org.example.model.User;
import org.example.repository.CounterReadingRepository;
import org.example.util.Format;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Обработчик методов показателя счетчика, также аудит на каждом действии
 */
public class CounterReadingService implements Service{
    /**
     * Поля инициализации
     */
    private CounterReadingRepository counterReadingRepository;
    private UserActionRepository userActionRepository;


    /**
     * Во время инстанса класса также инициализируется аудит и репозиторий показателей счетчика
     */
    public CounterReadingService() {
        this.userActionRepository = UserActionRepository.getInstance();
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
        if (!latestCounter.isEmpty()) {
            var latestCounterDTO = MapperCR.toDTO(latestCounter);
            if (!counterReading.compare(latestCounterDTO)) {
                UserAction userAction = new UserAction(currentUser.getUsername(), "Error of validation", LocalDateTime.now());
                userActionRepository.save(userAction);
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
                .collect(Collectors.toMap(key -> key, value -> 0.0));
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
                userActionRepository.save(userAction);
                return null;
            }
        }
        var counterReading = MapperCR.toEntity(counterReadingDTO);
        counterReadingRepository.save(counterReading);
        UserAction userAction = new UserAction(currentUser.getUsername(), "Submit Counter Reading success.", LocalDateTime.now());
        userActionRepository.save(userAction);
        return counterReadingDTO;
    }

    /**
     * Обработчик получения последних внесенных данных аутентифицированного пользователя
     *
     * @return CounterReading если все нормально; null если данные не найдены
     */
    public CounterReadingDTO getLastUserInfo(User currentUser) {
        int id = currentUser.getId();
        var lastCountingReading = counterReadingRepository.findLastCounterReading(id);
        if (!lastCountingReading.isEmpty()) {
            UserAction userAction = new UserAction(currentUser.getUsername(), "Get Latest Counter Reading success", LocalDateTime.now());
            userActionRepository.save(userAction);
            return MapperCR.toDTO(lastCountingReading);
        } else {
            UserAction userAction = new UserAction(currentUser.getUsername(), "Get Latest Counter Reading failed. Data not found", LocalDateTime.now());
            userActionRepository.save(userAction);
            return null;
        }
    }

    /**
     * Обработчик получения данных за последний месяц аутентифицированного пользователя
     *
     * @return CounterReading если все нормально; null если данные не найдены
     */
    public CounterReadingDTO getUserInfoForMonth(User currentUser, int month, int year) {
        int id = currentUser.getId();
        var counterReadingForMonth = counterReadingRepository.findCounterReadingForMonth(id, month, year);
        if (!counterReadingForMonth.isEmpty()) {
            UserAction userAction = new UserAction(currentUser.getUsername(), "Get Counter Reading For Month success", LocalDateTime.now());
            userActionRepository.save(userAction);
            return MapperCR.toDTO(counterReadingForMonth);
        } else {
            UserAction userAction = new UserAction(currentUser.getUsername(), "Get Counter Reading For Month failed. Data not found", LocalDateTime.now());
            userActionRepository.save(userAction);
            return null;
        }
    }

    /**
     * Обработчик получения истории вносимых данных аутентифицированного пользователя
     */
    public List<CounterReadingDTO> getCRByUser(User currentUser) {
        int id = currentUser.getId();
        var list = counterReadingRepository.findAllByUserId(id);
        UserAction userAction = new UserAction(currentUser.getUsername(), "Get All Counter Reading For Month", LocalDateTime.now());
        userActionRepository.save(userAction);
        return Format.formatter(list);
    }
}
