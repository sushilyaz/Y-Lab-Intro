package org.example.repository;

import org.example.model.CounterReading;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий показаний. Данные хранятся в листе. Реализован паттерн синглтон, вроде как множественного
 * * доступа к этому приложению не планируется, поэтому реализовал не потокобезопасный вариант
 */
public class CounterReadingRepository extends BaseRepository implements ReadingRepository{

    /**
     * Для синглтона
     */
    private static CounterReadingRepository instance;

    /**
     * приватный конструктор для синглтона
     */
    private CounterReadingRepository() {
    }

    /**
     * Для синглтона
     */
    public static CounterReadingRepository getInstance() {
        if (instance == null) {
            instance = new CounterReadingRepository();
        }
        return instance;
    }
    /**
     * поиск всех данных по id пользователя
     */
    public List<CounterReading> findAllByUserId(int userId) {
        String sql = "SELECT * FROM mainschema.counter_reading WHERE user_id = ? ORDER BY year DESC, month DESC";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            var resultSet = stmt.executeQuery();
            var results = new ArrayList<CounterReading>();
            while (resultSet.next()) {
                var counterReading = getCR(resultSet);
                results.add(counterReading);
            }
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
        String sql = "INSERT INTO mainschema.counter_reading (user_id, year, month, type, value) VALUES (?,?,?,?,?)";
        try (var stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (var element : counterReadings) {
                stmt.setInt(1, element.getUserId());
                stmt.setInt(2, element.getYear());
                stmt.setInt(3, element.getMonth());
                stmt.setString(4, element.getType());
                stmt.setDouble(5, element.getValue());
                stmt.executeUpdate();
                var generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    element.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Trouble with generate id");
                }
            }
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
        }
    }


    /**
     * Одно из требований к Д\З: "Последние поданые показания считаются актуальными".
     * Если проецировать на жизненную, ситуацию, то правильнее будет возвращать последнее по дате,
     * но я на всякий случай оставил закомментированное решение согласно ТЗ
     */
    public List<CounterReading> findLastCounterReading(int userId) {
        String sql = "SELECT * FROM mainschema.counter_reading WHERE user_id = ? ORDER BY year DESC, month DESC LIMIT ?";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            int limit = uniqueType(userId).size();
            stmt.setInt(2, limit);
            var resultSet = stmt.executeQuery();
            var results = new ArrayList<CounterReading>();
            while (resultSet.next()) {
                var counterReading = getCR(resultSet);
                results.add(counterReading);
            }
            return results;
        } catch (SQLException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Поиск данных по месяцу и году
     */
    public List<CounterReading> findCounterReadingForMonth(int userId, int month, int year) {
        String sql = "SELECT * FROM mainschema.counter_reading WHERE user_id = ? and year = ? and month = ?";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, year);
            stmt.setInt(3, month);
            var resultSet = stmt.executeQuery();
            var results = new ArrayList<CounterReading>();
            while (resultSet.next()) {
                var counterReading = getCR(resultSet);
                results.add(counterReading);
            }
            return results;
        } catch (SQLException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<String> uniqueType(int userId) {
        String sql = "SELECT DISTINCT type FROM mainschema.counter_reading WHERE user_id = ?";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            var resultSet = stmt.executeQuery();
            var results = new ArrayList<String>();
            while (resultSet.next()) {
                results.add(resultSet.getString("type"));
            }
            return results;
        } catch (SQLException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
            return new ArrayList<>();
        }
    }



    private CounterReading getCR(ResultSet resultSet) throws SQLException {
        var id = resultSet.getInt("id");
        var userId = resultSet.getInt("user_id");
        var year = resultSet.getInt("year");
        var month = resultSet.getInt("month");
        var type = resultSet.getString("type");
        var value = resultSet.getDouble("value");
        CounterReading counterReading = new CounterReading(id, userId, year, month, type, value);
        return counterReading;
    }
}
