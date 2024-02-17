package org.example.in.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.example.dto.AuthDTO;
import org.example.dto.UserCreateDTO;
import org.example.dto.UserDTO;
import org.example.model.User;
import org.example.service.UserService;
import org.example.utils.UserUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
public class UserControllerTest {
    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test_suhoi")
            .withUsername("test_suhoi")
            .withPassword("test_qwerty");
    private static Connection connection;
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private UserUtils userUtils;

    @InjectMocks
    private UserController userController;
    @BeforeAll
    static void setUp() throws SQLException, LiquibaseException {
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
    static void tearDown() throws SQLException {
        connection.rollback();
        connection.close();
        postgresContainer.stop();
    }
    @BeforeEach
    void setUpEach() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testCreateUserSuccess() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO("testUser", "testPassword");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testUser");

        when(userService.registerUser(any(UserCreateDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userCreateDTO)))
                .andExpect(status().isCreated());
    }
    @Test
    void testCreateUserConflict() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO("user1", "user1");

        when(userService.registerUser(any(UserCreateDTO.class))).thenReturn(null);

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userCreateDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    void testAuthUserSuccess() throws Exception {
        AuthDTO authDTO = new AuthDTO();
        authDTO.setUsername("user1");
        authDTO.setPassword("user1");

        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        when(userService.authenticationUser(any(AuthDTO.class))).thenReturn(user);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testAuthUserNotFound() throws Exception {
        AuthDTO authDTO = new AuthDTO();
        authDTO.setUsername("nonExistingUser");
        authDTO.setPassword("testPassword");

        when(userService.authenticationUser(any(AuthDTO.class))).thenReturn(null);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authDTO)))
                .andExpect(status().isNotFound());
    }
}
