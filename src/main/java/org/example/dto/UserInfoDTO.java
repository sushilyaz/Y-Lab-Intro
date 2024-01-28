package org.example.dto;

import java.util.Map;
import java.util.Objects;

/**
 * DTO для вывода всей информации о всех пользователях (для админа)
 * Не включил поле password, хотя для администратора можно было бы и добавить (наверное)
 */
public class UserInfoDTO {
    private String username;
    private int year;
    private int month;
    private Map<String, Double> typeOfCounter;

    public UserInfoDTO(String username, int year, int month, Map<String, Double> typeOfCounter) {
        this.username = username;
        this.year = year;
        this.month = month;
        this.typeOfCounter = typeOfCounter;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Map<String, Double> getTypeOfCounter() {
        return typeOfCounter;
    }

    public void setTypeOfCounter(Map<String, Double> typeOfCounter) {
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
     * For tests
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
