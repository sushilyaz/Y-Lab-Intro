package org.example.in.servlets.adminServlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.example.model.Role;
import org.example.model.User;
import org.example.repository.BaseRepository;
import org.example.service.AdminService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Testcontainers
public class AdminControllerTest {
    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test_suhoi")
            .withUsername("test_suhoi")
            .withPassword("test_qwerty");
    private static Connection connection;
    @InjectMocks
    private AddNewKey addNewKeyServlet;

    @InjectMocks
    private GetAllKeys getAllKeysServlet;

    @InjectMocks
    private GetAllUserData getAllUserDataServlet;

    @InjectMocks
    private GetAllUserInfoAndCR getAllUserInfoAndCRServlet;

    @InjectMocks
    private GetLastDataUser getLastDataUserServlet;

    @InjectMocks
    private GetUserDataForMonth getUserDataForMonth;

    @Mock
    HttpServletResponse response;
    @Mock
    HttpServletRequest request;

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
        BaseRepository.setConnection(connection);
        System.out.println("Migration is completed successfully");
        connection.setAutoCommit(false);
    }
    @AfterAll
    static void tearDown() throws SQLException {
        connection.rollback();
        connection.close();
        postgresContainer.stop();
    }

    @BeforeEach
    public void setUpEarlyTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNewKeyServletSuccess() throws ServletException, IOException {
        HttpSession sessionMock = mock(HttpSession.class);
        when(request.getSession(true)).thenReturn(sessionMock);

        User user1 = new User(1, "admin","admin", Role.ADMIN);
        when(sessionMock.getAttribute("user")).thenReturn(user1);

        StringReader stringReader = new StringReader("{\"newKey\":\"Gaz\"}");
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        when(request.getReader()).thenReturn(bufferedReader);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        addNewKeyServlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);

        AdminService adminService = new AdminService();
        List<String> keys = adminService.getAllKey();
        assertThat(keys).contains("Gaz");
    }

    @Test
    void testAddNewKeyServletFailed() throws ServletException, IOException {
        HttpSession sessionMock = mock(HttpSession.class);
        when(request.getSession(true)).thenReturn(sessionMock);

        User user1 = new User(1, "admin","admin", Role.ADMIN);
        when(sessionMock.getAttribute("user")).thenReturn(user1);

        StringReader stringReader = new StringReader("{\"newKey\":\"Heating\"}");
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        when(request.getReader()).thenReturn(bufferedReader);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        addNewKeyServlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
    @Test
    void testGetAllKeysServletSuccess() throws ServletException, IOException {
        HttpSession sessionMock = mock(HttpSession.class);
        when(request.getSession(true)).thenReturn(sessionMock);

        User user1 = new User(1, "admin","admin", Role.ADMIN);
        when(sessionMock.getAttribute("user")).thenReturn(user1);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writerMock = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writerMock);

        getAllKeysServlet.doGet(request, response);
        String key = "Heating";
        assertThat(stringWriter.toString()).contains(key);
    }
}
