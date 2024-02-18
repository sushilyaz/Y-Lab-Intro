package org.example.org.example.repository;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.example.config.MyConnectionPool;
import org.example.model.Role;
import org.example.model.User;
import org.example.repository.UserRepositoryImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@Testcontainers
@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {
    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test_suhoi")
            .withUsername("test_suhoi")
            .withPassword("test_qwerty");

    private static Connection connection;
    @Mock
    private MyConnectionPool myConnectionPool;

    @InjectMocks
    private UserRepositoryImpl userRepository;
    @BeforeEach
    void setUp() throws SQLException, LiquibaseException {
        postgresContainer.start();
        connection = DriverManager.getConnection(postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(), postgresContainer.getPassword());
        Database database =
                DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase liquibase =
                new Liquibase("db/changelog/changelogTest.xml", new ClassLoaderResourceAccessor(), database);
        liquibase.update();
        System.out.println("Migration is completed successfully");
    }

    @AfterAll
    static void tearDown() {
        postgresContainer.stop();
    }

    /**
     * Успешный сейв (новый юзер)
     * @throws Exception
     */
    @Test
    void testSave() throws Exception{
        when(myConnectionPool.getConnection()).thenReturn(connection);
        doNothing().when(myConnectionPool).returnConnection(connection);
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testUser");
        user.setRole(Role.SIMPLE_USER);
        userRepository.save(user);
        assertEquals(user.getId(), 4L);

    }

    @Test
    void testFindByUsername() throws Exception{
        when(myConnectionPool.getConnection()).thenReturn(connection);
        doNothing().when(myConnectionPool).returnConnection(any(Connection.class));
        User expected = new User();
        expected.setId(2L);
        expected.setUsername("user1");
        expected.setPassword("user1");
        expected.setRole(Role.SIMPLE_USER);
        Optional<User> actual = userRepository.findByUsername("user1");
        assertEquals(actual.get(), expected);
    }
}
