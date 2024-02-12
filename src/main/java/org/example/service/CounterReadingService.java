package org.example.service;

import org.example.dto.CounterReadingCreateDTO;
import org.example.dto.CounterReadingDTO;
import org.example.mapper.CounterReadingMapper;
import org.example.model.CounterReading;
import org.example.model.User;
import org.example.model.UserAction;
import org.example.repository.CounterReadingRepository;
import org.example.repository.UserActionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        List<String> allKeys = counterReadingRepository.uniqueType(1);
        Set<String> keySetIn = counterReading.getTypeOfCounter().keySet();
        if (keySetIn.size() != allKeys.size()) {
            return null;
        }
        for (String element : allKeys) {
            if (!keySetIn.contains(element)) {
                return null;
            }
        }
        List<CounterReading> latestCounter = counterReadingRepository.findLastCounterReading(id);
        if (!latestCounter.isEmpty()) {
            CounterReadingDTO latestCounterDTO = CounterReadingMapper.INSTANCE.map(latestCounter);
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


    /**
     * Получение всех типов показаний (только ключей)
     * Здесь идея в том, что новые показания добавляются к пользователю admin(администратор)
     * Он грубо говоря является неким центром, я посчитал, что так будет очень удобно, поэтому в uniqueType
     * передается параметр userId (1) - это администратор
     * Также преобразование листа в мапу для контроллера
     */
    public Map<String, Double> getTypeOfCounter() {
        List<String> data = counterReadingRepository.uniqueType(1);
        return data.stream()
                .collect(Collectors.toMap(key -> key, value -> 0.0));
    }

    /**
     * Обработчик внесения данных аутентифицированного пользователя
     *
     * @return CounterReadingDTO, если все нормально; null - если данные в этот месяц уже вносились
     */
    public CounterReadingDTO submitCounterReading(User currentUser, CounterReadingCreateDTO dtoCreate) {
        int id = currentUser.getId();
        List<CounterReading> counterList = counterReadingRepository.findAllByUserId(id);
        for (CounterReading counter : counterList) {
            if (counter.getMonth() == dtoCreate.getMonth() && counter.getYear() == dtoCreate.getYear()) {
                UserAction userAction = new UserAction(currentUser.getUsername(), "Submit Counter Reading failed. Data already exist.", LocalDateTime.now());
                userActionRepository.save(userAction);
                return null;
            }
        }
        List<CounterReading> counterReading = CounterReadingMapper.INSTANCE.map(dtoCreate, id);
        counterReadingRepository.save(counterReading);
        UserAction userAction = new UserAction(currentUser.getUsername(), "Submit Counter Reading success.", LocalDateTime.now());
        userActionRepository.save(userAction);
        return CounterReadingMapper.INSTANCE.map(counterReading);
    }

    /**
     * Обработчик получения последних внесенных данных аутентифицированного пользователя
     *
     * @return CounterReadingDTO если все нормально; null если данные не найдены
     */
    public CounterReadingDTO getLastUserInfo(User currentUser) {
        int id = currentUser.getId();
        List<CounterReading> lastCountingReading = counterReadingRepository.findLastCounterReading(id);
        if (!lastCountingReading.isEmpty()) {
            UserAction userAction = new UserAction(currentUser.getUsername(), "Get Latest Counter Reading success", LocalDateTime.now());
            userActionRepository.save(userAction);
            return CounterReadingMapper.INSTANCE.map(lastCountingReading);
        } else {
            UserAction userAction = new UserAction(currentUser.getUsername(), "Get Latest Counter Reading failed. Data not found", LocalDateTime.now());
            userActionRepository.save(userAction);
            return null;
        }
    }

    /**
     * Обработчик получения данных за последний месяц аутентифицированного пользователя
     *
     * @return CounterReadingDTO если все нормально; null если данные не найдены
     */
    public CounterReadingDTO getUserInfoForMonth(User currentUser, int month, int year) {
        int id = currentUser.getId();
        List<CounterReading> counterReadingForMonth = counterReadingRepository.findCounterReadingForMonth(id, month, year);
        if (!counterReadingForMonth.isEmpty()) {
            UserAction userAction = new UserAction(currentUser.getUsername(), "Get Counter Reading For Month success", LocalDateTime.now());
            userActionRepository.save(userAction);
            return CounterReadingMapper.INSTANCE.map(counterReadingForMonth);
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
        List<CounterReading> entities = counterReadingRepository.findAllByUserId(id);
        UserAction userAction = new UserAction(currentUser.getUsername(), "Get All Counter Reading For Month", LocalDateTime.now());
        userActionRepository.save(userAction);
        return CounterReadingMapper.INSTANCE.toListDTO(entities);
    }
}
