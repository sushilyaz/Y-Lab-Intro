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
import org.example.repository.UserRepository;
import org.example.service.AdminService;
import org.example.service.CounterReadingService;
import org.example.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AdminServiceTest {
    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test_suhoi")
            .withUsername("test_suhoi")
            .withPassword("test_qwerty");
    private static Connection connection;
    private AdminService adminService = new AdminService();
    private UserRepository userRepository = UserRepository.getInstance();

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
        init();
    }

    @AfterAll
    static void tearDown() throws SQLException {
        connection.rollback();
        connection.close();
        postgresContainer.stop();
    }

    static void init() {
        UserService userService = new UserService();
        var counterReadingService = new CounterReadingService();
        User user1 = userService.registerUser("testUser1", "testUser1");
        user1.setId(2);
        Map<String, Double> data1 = new HashMap<>();
        data1.put("Heating", 200.0);
        data1.put("Cold Water", 200.0);
        data1.put("Hot Water", 200.0);
        counterReadingService.submitCounterReading(user1, new CounterReadingDTO(2, 2023, 5, data1));
        Map<String, Double> data2 = new HashMap<>();
        data2.put("Heating", 300.0);
        data2.put("Cold Water", 300.0);
        data2.put("Hot Water", 300.0);
        counterReadingService.submitCounterReading(user1, new CounterReadingDTO(2, 2023, 6, data2));
        User user2 = userService.registerUser("testUser2", "testUser2");
        user1.setId(3);
        Map<String, Double> data3 = new HashMap<>();
        data3.put("Heating", 200.0);
        data3.put("Cold Water", 200.0);
        data3.put("Hot Water", 200.0);
        counterReadingService.submitCounterReading(user2, new CounterReadingDTO(3, 2022, 1, data3));
    }

    @Test
    void testGetCRByUser() {
        var user = userRepository.findByUsername("testUser1").get();
        var data = adminService.getCRByUser(user);
        assertThat(data.get(0).getYear()).isEqualTo(2023);
        assertThat(data.get(1).getTypeOfCounter().get("Heating")).isEqualTo(200.0);
        assertThat(data.get(1).getTypeOfCounter().get("Cold Water")).isEqualTo(200.0);
        assertThat(data.get(1).getTypeOfCounter().get("Hot Water")).isEqualTo(200.0);
    }

    @Test
    void testLastUserInfo() {
        var user = userRepository.findByUsername("testUser1").get();
        var data = adminService.getLastUserInfo(user);
        assertThat(data.getTypeOfCounter().get("Heating")).isEqualTo(300.0);
    }

    @Test
    void testUserInfoForMonth() {
        var user = userRepository.findByUsername("testUser1").get();
        var data = adminService.getUserInfoForMonth(user, 5,2023);
        assertThat(data.getTypeOfCounter().get("Heating")).isEqualTo(200.0);
        assertThat(data.getTypeOfCounter().get("Cold Water")).isEqualTo(200.0);
        assertThat(data.getTypeOfCounter().get("Hot Water")).isEqualTo(200.0);
    }

    @Test
    void testKeyUpdate() {
        var startKeyData = adminService.getAllKey();
        assertThat(startKeyData).contains("Heating");
        assertThat(startKeyData).contains("Cold Water");
        assertThat(startKeyData).contains("Hot Water");
        assertThat("Gaz").isNotIn(startKeyData);
        adminService.addNewKey("Gaz");
        startKeyData = adminService.getAllKey();
        assertThat(startKeyData).contains("Gaz");
    }
}
