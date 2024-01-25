package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private List<CounterReading> counterReadings;
    private boolean isAuthenticated;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.counterReadings = new ArrayList<>();
        this.isAuthenticated = false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<CounterReading> getCounterReadings() {
        return counterReadings;
    }

    public void setCounterReadings(List<CounterReading> counterReadings) {
        this.counterReadings = counterReadings;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }
}
