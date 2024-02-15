package org.example.repository;

import org.example.config.MyConnectionPool;
import org.example.model.CounterReading;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CounterReadingRepositoryImpl implements CounterReadingRepository {

    public List<CounterReading> findAllByUserId(Long userId) {
        String sql = "SELECT * FROM mainschema.counter_reading WHERE user_id = ? ORDER BY date DESC";
        try (Connection connection = MyConnectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet resultSet = stmt.executeQuery();
            List<CounterReading> results = new ArrayList<>();
            while (resultSet.next()) {
                CounterReading counterReading = getCR(resultSet);
                results.add(counterReading);
            }
            MyConnectionPool.releaseConnection(connection);
            return results;
        } catch (SQLException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Сохранение в лист показаний
     */
    public void save(List<CounterReading> counterReadings) {
        String sql = "INSERT INTO mainschema.counter_reading (id, user_id, date, type, value) VALUES (nextval('mainschema.seq_cr'),?,?,?,?)";
        try (Connection connection = MyConnectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (CounterReading element : counterReadings) {
                stmt.setLong(1, element.getUserId());
                stmt.setDate(2, Date.valueOf(element.getDate()));
                stmt.setString(3, element.getType());
                stmt.setDouble(4, element.getValue());
                stmt.executeUpdate();
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    element.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Trouble with generate id");
                }
            }
            connection.commit();
            MyConnectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
        }
    }


    /**
     * Актуальные значения показаний счетчика пользователя
     */
    public List<CounterReading> findLastCounterReading(Long userId) {
        String sql = "SELECT * FROM mainschema.counter_reading WHERE user_id = ? ORDER BY date DESC LIMIT ?";
        try (Connection connection = MyConnectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            int limit = uniqueType(userId).size();
            stmt.setInt(2, limit);
            ResultSet resultSet = stmt.executeQuery();
            List<CounterReading> results = new ArrayList<>();
            while (resultSet.next()) {
                CounterReading counterReading = getCR(resultSet);
                results.add(counterReading);
            }
            MyConnectionPool.releaseConnection(connection);
            return results;
        } catch (SQLException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Поиск данных по месяцу и году
     */
    public List<CounterReading> findCounterReadingForMonth(Long userId, LocalDate date) {
        String sql = "SELECT * FROM mainschema.counter_reading WHERE user_id = ? and date = ?";
        try (Connection connection = MyConnectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setDate(2, Date.valueOf(date));
            ResultSet resultSet = stmt.executeQuery();
            List<CounterReading> results = new ArrayList<>();
            while (resultSet.next()) {
                CounterReading counterReading = getCR(resultSet);
                results.add(counterReading);
            }
            MyConnectionPool.releaseConnection(connection);
            return results;
        } catch (SQLException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Получение типов показаний счетчика
     */
    public List<String> uniqueType(Long userId) {
        String sql = "SELECT DISTINCT type FROM mainschema.counter_reading WHERE user_id = ?";
        try (Connection connection = MyConnectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet resultSet = stmt.executeQuery();
            List<String> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(resultSet.getString("type"));
            }
            MyConnectionPool.releaseConnection(connection);
            return results;
        } catch (SQLException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
            return new ArrayList<>();
        }

    }


    /**
     * Преобрвазование в сущность
     */
    private CounterReading getCR(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Long userId = resultSet.getLong("user_id");
        LocalDate date = resultSet.getDate("date").toLocalDate();
        String type = resultSet.getString("type");
        Double value = resultSet.getDouble("value");
        CounterReading counterReading = new CounterReading();
        counterReading.setId(id);
        counterReading.setUserId(userId);
        counterReading.setDate(date);
        counterReading.setType(type);
        counterReading.setValue(value);
        return counterReading;
    }
}
