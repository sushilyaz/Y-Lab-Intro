package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Модель показаний счетчика.
 */
@AllArgsConstructor
@Getter
@Setter
public class CounterReading {
    private int id;

    /**
     * Поле зависимости от userId
     */
    private int userId;
    /**
     * Поле года вносимых показаний
     */
    private int year;
    /**
     * Поле месяца вносимых показаний
     */
    private int month;
    /**
     * Поле показаний. Это static поле необходимо для того, если админ в рантайме добавил новые виды показаний,
     * у обычного пользователя "обновился" список типов показаний.
     * Также это поле необходимо для инициализации начальных значений (применяется в CounterReadingController.putData)
     */
    private String type;
    private double value;

}
