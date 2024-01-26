package org.example.model;

import java.util.Objects;

public class CounterReading {
    private int userId;
    private int year;
    private int month;
    private TypeOfCounter typeOfCounter;

    public CounterReading( int year, int month, TypeOfCounter typeOfCounter) {
        this.year = year;
        this.month = month;
        this.typeOfCounter = typeOfCounter;
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

    public TypeOfCounter getTypeOfCounter() {
        return typeOfCounter;
    }

    public void setTypeOfCounter(TypeOfCounter typeOfCounter) {
        this.typeOfCounter = typeOfCounter;
    }

    @Override
    public String toString() {
        return "Counter Reading: " +
                "year = " + year +
                ", month = " + month +
                ", " + typeOfCounter;
    }

    // for tests
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CounterReading that = (CounterReading) o;
        return userId == that.userId && year == that.year && month == that.month && Objects.equals(typeOfCounter, that.typeOfCounter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, year, month, typeOfCounter);
    }
}
