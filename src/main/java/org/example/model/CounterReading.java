package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Модель показаний счетчика.
 * Долго думал, как лучше сделать. Создать еще одну таблицу с полями type и value. Или оставить все в одной таблице
 * Решил сделать все в одной таблице. Как бы вы сделали?
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
     * Поле типа показаний
     */
    private String type;
    /**
     * Поле значения показания
     */
    private double value;

}
