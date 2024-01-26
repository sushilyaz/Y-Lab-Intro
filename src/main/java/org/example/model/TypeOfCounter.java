package org.example.model;

import java.util.Objects;

public class TypeOfCounter {
    private double hotWater;
    private double coldWater;
    private double heating;

    public TypeOfCounter(double hotWater, double coldWater, double heating) {
        this.hotWater = hotWater;
        this.coldWater = coldWater;
        this.heating = heating;
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
    // for tests
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeOfCounter that = (TypeOfCounter) o;
        return Double.compare(hotWater, that.hotWater) == 0 && Double.compare(coldWater, that.coldWater) == 0 && Double.compare(heating, that.heating) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hotWater, coldWater, heating);
    }

    @Override
    public String toString() {
        return "TypeOfCounter: " +
                "hotWater = " + hotWater +
                ", coldWater = " + coldWater +
                ", heating = " + heating +
                "";
    }
}
