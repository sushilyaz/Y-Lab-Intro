package org.example.dto;

import java.util.Objects;

/**
 * DTO для вывода всей информации о всех пользователях (для админа)
 * Не включил поле password, хотя для администратора можно было бы и добавить (наверное)
 */
public class UserInfoDTO {
    private String username;
    private int year;
    private int month;
    private double hotWater;
    private double coldWater;
    private double heating;

    public UserInfoDTO(String username, int year, int month, double hotWater, double coldWater, double heating) {
        this.username = username;
        this.year = year;
        this.month = month;
        this.hotWater = hotWater;
        this.coldWater = coldWater;
        this.heating = heating;
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

    public double getHotWater() {
        return hotWater;
    }

    public void setHotWater(double hotWater) {
        this.hotWater = hotWater;
    }

    public double getColdWater() {
        return coldWater;
    }

    public void setColdWater(double coldWater) {
        this.coldWater = coldWater;
    }

    public double getHeating() {
        return heating;
    }

    public void setHeating(double heating) {
        this.heating = heating;
    }

    /**
     * Для sout
     */
    @Override
    public String toString() {
        return "UsersInfo: \n" +
                "username='" + username + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", hotWater=" + hotWater +
                ", coldWater=" + coldWater +
                ", heating=" + heating +
                '\n';
    }

    /**
     * Для тестирования
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfoDTO that = (UserInfoDTO) o;
        return year == that.year && month == that.month && Double.compare(hotWater, that.hotWater) == 0 && Double.compare(coldWater, that.coldWater) == 0 && Double.compare(heating, that.heating) == 0 && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, year, month, hotWater, coldWater, heating);
    }
}
