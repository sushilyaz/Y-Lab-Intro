package org.example.model;

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
        return "CounterReading{" +
                "year=" + year +
                ", month=" + month +
                ", typeOfCounter=" + typeOfCounter +
                '}';
    }
}
