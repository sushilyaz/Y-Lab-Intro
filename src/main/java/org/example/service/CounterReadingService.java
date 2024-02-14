package org.example.service;

import org.example.dto.CounterReadingCreateDTO;
import org.example.dto.CounterReadingDTO;
import org.example.mapper.CounterReadingMapper;
import org.example.model.CounterReading;
import org.example.model.User;
import org.example.repository.CounterReadingRepository;
import org.example.repository.UserActionRepository;

import java.util.ArrayList;
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
                return null;
            }
        }
        List<CounterReading> counterReading = CounterReadingMapper.INSTANCE.map(dtoCreate, id);
        counterReadingRepository.save(counterReading);
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
            return CounterReadingMapper.INSTANCE.map(lastCountingReading);
        } else {
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
            return CounterReadingMapper.INSTANCE.map(counterReadingForMonth);
        } else {
            return null;
        }
    }

    /**
     * Обработчик получения истории вносимых данных аутентифицированного пользователя
     */
    public List<CounterReadingDTO> getCRByUser(User currentUser) {
        int id = currentUser.getId();
        List<CounterReading> entities = counterReadingRepository.findAllByUserId(id);
        if (entities.isEmpty()) {
            return new ArrayList<CounterReadingDTO>();
        } else {
            return CounterReadingMapper.INSTANCE.toListDTO(entities);
        }
    }
}
