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
    private int userId;
    private int year;
    private int month;
    private String type;
    private double value;

    public CounterReading() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
