package org.example.service;

import org.example.dto.CounterReadingCreateDTO;
import org.example.dto.CounterReadingDTO;
import org.example.mapper.CounterReadingMapper;
import org.example.model.CounterReading;
import org.example.model.User;
import org.example.repository.CounterReadingRepository;
import org.example.repository.CounterReadingRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Обработчик методов показателя счетчика, также аудит на каждом действии
 */
@Component
public class CounterReadingServiceImpl implements CounterReadingService {
    private final CounterReadingRepository counterReadingRepository;
    private final CounterReadingMapper counterReadingMapper;

    @Autowired
    public CounterReadingServiceImpl(CounterReadingRepositoryImpl counterReadingRepository, CounterReadingMapper counterReadingMapper) {
        this.counterReadingRepository = counterReadingRepository;
        this.counterReadingMapper = counterReadingMapper;
    }

    /**
     * Валидация вводимых показаний. Ни одно показание за этот месяц не должно быть меньше показаний предыдущего
     *
     * @return CounterReading если ошибок валидации нет; null - если ошибка валидации
     */
    public List<CounterReadingCreateDTO> validationCounter(User currentUser, List<CounterReadingCreateDTO> counterReading) {
        Long id = currentUser.getId();
        List<String> allKeys = counterReadingRepository.uniqueType(1L);
        List<String> keysIn = counterReading.stream()
                .map(CounterReadingCreateDTO::getType)
                .toList();
        if (keysIn.size() != allKeys.size()) {
            return new ArrayList<>();
        }
        for (String element : allKeys) {
            if (!keysIn.contains(element)) {
                return new ArrayList<>();
            }
        }
        List<CounterReading> latestCounter = counterReadingRepository.findLastCounterReading(id);
        if (!latestCounter.isEmpty()) {
            List<CounterReadingDTO> latestCounterDTO = counterReadingMapper.entitiesToDTOs(latestCounter);
            for (int i = 0; i < counterReading.size(); i++) {
                CounterReadingCreateDTO element = counterReading.get(i);
                CounterReadingDTO latestElement = latestCounterDTO.get(i);
                if (element.getValue() < latestElement.getValue()) {
                    return new ArrayList<>();
                }
            }
            return counterReading;
        } else {
            return counterReading;
        }
    }


    /**
     * Обработчик внесения данных аутентифицированного пользователя
     *
     * @return CounterReadingDTO, если все нормально; null - если данные в этот месяц уже вносились
     */
    public List<CounterReadingDTO> submitCounterReading(User currentUser, List<CounterReadingCreateDTO> dtoCreate) {
        Long id = currentUser.getId();
        List<CounterReading> counterList = counterReadingRepository.findAllByUserId(id);
        for (CounterReading counter : counterList) {
            if (counter.getDate().equals(dtoCreate.get(0).getDate())) {
                return new ArrayList<>();
            }
        }
        List<CounterReading> counterReading = counterReadingMapper.map(dtoCreate, id);
        counterReadingRepository.save(counterReading);
        return counterReadingMapper.entitiesToDTOs(counterReading);
    }

    /**
     * Обработчик получения последних внесенных данных аутентифицированного пользователя
     *
     * @return CounterReadingDTO если все нормально; null если данные не найдены
     */
    public List<CounterReadingDTO> getLastUserInfo(User currentUser) {
        Long id = currentUser.getId();
        List<CounterReading> lastCountingReading = counterReadingRepository.findLastCounterReading(id);
        if (!lastCountingReading.isEmpty()) {
            return counterReadingMapper.entitiesToDTOs(lastCountingReading);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Обработчик получения данных за последний месяц аутентифицированного пользователя
     *
     * @return CounterReadingDTO если все нормально; null если данные не найдены
     */
    public List<CounterReadingDTO> getUserInfoForMonth(User currentUser, LocalDate date) {
        Long id = currentUser.getId();
        List<CounterReading> counterReadingForMonth = counterReadingRepository.findCounterReadingForMonth(id, date);
        if (!counterReadingForMonth.isEmpty()) {
            return counterReadingMapper.entitiesToDTOs(counterReadingForMonth);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Обработчик получения истории вносимых данных аутентифицированного пользователя
     */
    public List<CounterReadingDTO> getCRByUser(User currentUser) {
        Long id = currentUser.getId();
        List<CounterReading> entities = counterReadingRepository.findAllByUserId(id);
        if (entities.isEmpty()) {
            return new ArrayList<>();
        } else {
            return counterReadingMapper.entitiesToDTOs(entities);
        }
    }
}
