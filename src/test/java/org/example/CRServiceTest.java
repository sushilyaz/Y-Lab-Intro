package org.example;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.example.dto.CounterReadingDTO;
import org.example.model.User;
import org.example.repository.BaseRepository;
import org.example.service.CounterReadingService;
import org.example.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * Тестирование сервиса подачи показаний
 */
@Testcontainers
public class CRServiceTest {
    /**
     * Создание тестового контейнера с подключением
     */
    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test_suhoi")
            .withUsername("test_suhoi")
            .withPassword("test_qwerty");
    private static Connection connection;
    CounterReadingService counterReadingService = new CounterReadingService();

    private static User currentUser;
    /**
     *  Установка соединение с контейнером, передача соединения BaseRepository и выполнение скриптов миграции
     */
    @BeforeAll
    static void setUp() throws SQLException, LiquibaseException {
        postgresContainer.start();
        connection = DriverManager.getConnection(postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(), postgresContainer.getPassword());
        Database database =
                DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase liquibase =
                new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
        liquibase.update();
        BaseRepository.connection = connection;
        System.out.println("Migration is completed successfully");
        connection.setAutoCommit(false);
        initUser();
    }

    /**
     *  Закрытие соединения
     */
    @AfterAll
    static void tearDown() throws SQLException {
        connection.rollback();
        connection.close();
        postgresContainer.stop();
    }
    /**
     *  Инициализация
     */
    static void initUser() {
        UserService userService = new UserService();
        String username = "testUser";
        String password = "testUser";
        User user = userService.registerUser(username, password);
        user.setId(2);
        currentUser = user;
    }
    /**
     *  Тест на отправку данных и валидацию
     */
    @Test
    void validAndSubmit() {
        Map<String, Double> data1 = new HashMap<>();
        data1.put("Cold Water", 100.0);
        data1.put("Hot Water", 100.0);
        data1.put("Heating", 100.0);
        CounterReadingDTO dto1 = new CounterReadingDTO(currentUser.getId(), 2020, 4, data1);
        var actual1 = counterReadingService.submitCounterReading(currentUser, dto1);
        assertThat(actual1).isEqualTo(dto1);

        Map<String, Double> data2 = new HashMap<>();
        data2.put("Cold Water", 50.0);
        data2.put("Hot Water", 100.0);
        data2.put("Heating", 100.0);
        CounterReadingDTO dto2 = new CounterReadingDTO(currentUser.getId(), 2020, 5, data2);
        var actual2 = counterReadingService.validationCounter(currentUser, dto2);
        assertThat(actual2).isNull();

        CounterReadingDTO dto3 = new CounterReadingDTO(currentUser.getId(), 2020, 4, data2);
        var actual3 = counterReadingService.submitCounterReading(currentUser, dto3);
        assertThat(actual3).isNull();
    }

    /**
     *  Тест на получение актуальных показаний пользователя
     */
    @Test
    void latestCR() {
        Map<String, Double> data1 = new HashMap<>();
        data1.put("Cold Water", 200.0);
        data1.put("Hot Water", 200.0);
        data1.put("Heating", 200.0);
        CounterReadingDTO dto1 = new CounterReadingDTO(2, 2020, 5, data1);
        counterReadingService.submitCounterReading(currentUser, dto1);
        var actual = counterReadingService.getLastUserInfo(currentUser);
        assertThat(actual).isEqualTo(dto1);
    }
    /**
     *  Тест на получение показаний пользователя за конкретный месяц
     */
    @Test
    void forMonth() {
        var dto = counterReadingService.getUserInfoForMonth(currentUser, 4, 2020);
        assertThat(dto.getMonth()).isEqualTo(4);
        assertThat(dto.getYear()).isEqualTo(2020);
    }
}
