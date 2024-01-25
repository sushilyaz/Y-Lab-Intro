package org.example.model;

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
}
