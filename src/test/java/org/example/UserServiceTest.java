
package org.example;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.example.model.User;
import org.example.repository.BaseRepository;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тестировал только пограничные значения
 */
@Testcontainers
public class UserServiceTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("suhoi")
            .withUsername("suhoi")
            .withPassword("qwerty");
    private Connection connection;
    UserService userService = new UserService();
    UserRepository userRepository = UserRepository.getInstance();

    @BeforeEach
    void setUp() throws SQLException, LiquibaseException {
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
    }
    User initUsers() {
        String username = "testUser";
        String password = "testUser";
        User user = userService.registerUser(username, password);
        return user;
    }
    @AfterEach
    void tearDown() throws SQLException {
        connection.rollback();
        connection.close();
        postgresContainer.stop();
    }

    @Test
    void registerSuccess() throws SQLException {
        User user = initUsers();
        assertThat(user).isNotNull();
        var check = userRepository.findByUsername("testUser").get();
        assertThat(user).isEqualTo(check);
    }

    @Test
    void registerFailed() throws SQLException {
        User testUser = initUsers();
        User user = userService.registerUser("testUser", "testUser");
        assertThat(user).isNull();
    }

    @Test
    void authSuccess() throws SQLException {
        User user = initUsers();
        var check = userService.authenticationUser("testUser", "testUser");
        assertThat(check).isNotNull();
        assertThat(check).isEqualTo(user);
    }

    @Test
    void authFailed() throws SQLException {
        initUsers();
        var check = userService.authenticationUser("testUser", "failedPassword");
        assertThat(check).isNull();
    }
}
