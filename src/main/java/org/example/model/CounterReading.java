package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
public class CounterReading {
    private Long id;
    private Long userId;
    private LocalDate date;
    private String type;
    private Double value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CounterReading that = (CounterReading) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(date, that.date) && Objects.equals(type, that.type) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, date, type, value);
    }
}
