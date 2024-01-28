package org.example.dto;

import java.util.Map;
import java.util.Objects;

/**
 * DTO для вывода всей информации о всех пользователях (для админа)
 * Не включил поле password, хотя для администратора можно было бы и добавить (наверное)
 */
public class UserInfoDTO {
    /**
     * Имя пользователя
     */
    private String username;
    /**
     * Год внесения показаний
     */
    private int year;
    /**
     * Месяц внесения показаний
     */
    private int month;
    /**
     * Показания
     */
    private Map<String, Double> typeOfCounter;

    /**
     * Конструктор
     */
    public UserInfoDTO(String username, int year, int month, Map<String, Double> typeOfCounter) {
        this.username = username;
        this.year = year;
        this.month = month;
        this.typeOfCounter = typeOfCounter;
    }

    /**
     * Для sout
     */
    @Override
    public String toString() {
        return "UserInfoDTO{" +
                "username='" + username + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", typeOfCounter=" + typeOfCounter +
                "\n";
    }

    /**
     * Для тестов
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfoDTO that = (UserInfoDTO) o;
        return year == that.year && month == that.month && Objects.equals(username, that.username) && Objects.equals(typeOfCounter, that.typeOfCounter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, year, month, typeOfCounter);
    }
}
