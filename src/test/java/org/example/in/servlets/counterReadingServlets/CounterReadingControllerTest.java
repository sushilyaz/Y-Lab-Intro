package org.example.in.servlets.counterReadingServlets;

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
import org.example.dto.CounterReadingDTO;
import org.example.model.Role;
import org.example.model.User;
import org.example.repository.BaseRepository;
import org.example.service.CounterReadingService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Testcontainers
public class CounterReadingControllerTest {
    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test_suhoi")
            .withUsername("test_suhoi")
            .withPassword("test_qwerty");
    private static Connection connection;
    /**
     * Мокаю сервлеты и response, request
     */
    @InjectMocks
    private GetAllData getAllDataServlet;

    @InjectMocks
    private GetDataForMonth getDataForMonthServlet;

    @InjectMocks
    private PutData putDataServlet;

    @InjectMocks
    private GetLatestData getLatestDataServlet;

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
    static void deleteDataFromCR() throws LiquibaseException {
        Database database =
                DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase liquibase =
                new Liquibase("db/changelog/004-delete-data-after-test.xml", new ClassLoaderResourceAccessor(), database);
        liquibase.update();
    }

    @AfterAll
    static void tearDown() throws SQLException {
        connection.rollback();
        connection.close();
        postgresContainer.stop();
    }

    /**
     * Инициализирую моки
     */
    @BeforeEach
    public void setUpEarlyTest() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Получилось получить данные пользователя user1 (см. 003-insert-test-data.xml)
     * @throws IOException
     * @throws ServletException
     */
    @Test
    public void testGetAllDataServletSuccess() throws IOException, ServletException {
        HttpSession sessionMock = mock(HttpSession.class);
        when(request.getSession(true)).thenReturn(sessionMock);

        User user1 = new User(2, "user1","user1", Role.SIMPLE_USER);
        when(sessionMock.getAttribute("user")).thenReturn(user1);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        getAllDataServlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Не получить данные пользователя user2, так как у него их нет (см. 003-insert-test-data.xml)
     * @throws IOException
     * @throws ServletException
     */
    @Test
    public void testGetAllDataServletFailed() throws IOException, ServletException {
        HttpSession sessionMock = mock(HttpSession.class);
        when(request.getSession(true)).thenReturn(sessionMock);

        User user1 = new User(3, "user2","user2", Role.SIMPLE_USER);
        when(sessionMock.getAttribute("user")).thenReturn(user1);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        getAllDataServlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    /**
     * Получилось получить данные пользователя user1 за конкретный месяц (см. 003-insert-test-data.xml)
     * @throws IOException
     * @throws ServletException
     */
    @Test
    public void testGetDataForMonthSuccess() throws IOException, ServletException {
        HttpSession sessionMock = mock(HttpSession.class);
        when(request.getSession(true)).thenReturn(sessionMock);

        StringReader stringReader = new StringReader("{\"month\":\"1\",\"year\":\"2023\"}");
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        when(request.getReader()).thenReturn(bufferedReader);

        User user1 = new User(2, "user1","user1", Role.SIMPLE_USER);
        when(sessionMock.getAttribute("user")).thenReturn(user1);

        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        getDataForMonthServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Не получилось достать данные пользователя user1 за конкретный месяц, так как их нет(см. 003-insert-test-data.xml)
     * @throws IOException
     * @throws ServletException
     */
    @Test
    public void testGetDataForMonthFailed1() throws IOException, ServletException {
        HttpSession sessionMock = mock(HttpSession.class);
        when(request.getSession(true)).thenReturn(sessionMock);

        StringReader stringReader = new StringReader("{\"month\":\"5\",\"year\":\"2023\"}");
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        when(request.getReader()).thenReturn(bufferedReader);

        User user1 = new User(2, "user1","user1", Role.SIMPLE_USER);
        when(sessionMock.getAttribute("user")).thenReturn(user1);

        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        getDataForMonthServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
    /**
     * Не получилось достать данные пользователя user1 за конкретный месяц, так как данные не валидны (см. 003-insert-test-data.xml)
     * @throws IOException
     * @throws ServletException
     */
    @Test
    public void testGetDataForMonthFailed2() throws IOException, ServletException {
        HttpSession sessionMock = mock(HttpSession.class);
        when(request.getSession(true)).thenReturn(sessionMock);

        StringReader stringReader = new StringReader("{\"month\":\"0\",\"year\":\"2023\"}");
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        when(request.getReader()).thenReturn(bufferedReader);

        User user1 = new User(2, "user1","user1", Role.SIMPLE_USER);
        when(sessionMock.getAttribute("user")).thenReturn(user1);

        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        getDataForMonthServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
    /**
     * Получилось достать последние внесенные данные пользователя user1 (см. 003-insert-test-data.xml)
     * @throws IOException
     * @throws ServletException
     */
    @Test
    public void testGetLatestDataServletSuccess() throws IOException, ServletException {
        HttpSession sessionMock = mock(HttpSession.class);
        when(request.getSession(true)).thenReturn(sessionMock);

        User user1 = new User(2, "user1","user1", Role.SIMPLE_USER);
        when(sessionMock.getAttribute("user")).thenReturn(user1);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writerMock = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writerMock);

        getLatestDataServlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);

        String expectedJson = "{\"year\":2023,\"month\":2,\"typeOfCounter\":{\"Hot Water\":200.0,"
                + "\"Heating\":200.0,\"Cold Water\":200.0}}";

        assertEquals(expectedJson, stringWriter.toString());
    }
    /**
     * Получилось достать последние внесенные данные пользователя user2, так как их нет (см. 003-insert-test-data.xml)
     * @throws IOException
     * @throws ServletException
     */
    @Test
    public void testGetLatestDataServletFailed() throws IOException, ServletException {
        HttpSession sessionMock = mock(HttpSession.class);
        when(request.getSession(true)).thenReturn(sessionMock);

        User user1 = new User(3, "user2","user2", Role.SIMPLE_USER);
        when(sessionMock.getAttribute("user")).thenReturn(user1);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writerMock = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writerMock);

        getLatestDataServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    /**
     * Получилось положить данные в БД (см. 003-insert-test-data.xml)
     * @throws IOException
     * @throws ServletException
     */
    @Test
    public void testPutDataSuccess() throws IOException, ServletException, SQLException, LiquibaseException {
        HttpSession sessionMock = mock(HttpSession.class);
        when(request.getSession(true)).thenReturn(sessionMock);

        User user1 = new User(3, "user2","user2", Role.SIMPLE_USER);
        when(sessionMock.getAttribute("user")).thenReturn(user1);

        String data = "{\"year\":2023,\"month\":3,\"typeOfCounter\":{\"Hot Water\":300.0,"
                + "\"Heating\":300.0,\"Cold Water\":300.0}}";
        StringReader stringReader = new StringReader(data);
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        when(request.getReader()).thenReturn(bufferedReader);

        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        putDataServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);

        // проверка, что данные действительно сохранились. Изначально у user2 не было данных, поэтому использую метод getLastUserInfo
        CounterReadingService service = new CounterReadingService();
        CounterReadingDTO dto = service.getLastUserInfo(user1);

        assertThat(dto).isNotNull();
        // ниже так сказать откат БД (реализовал через ликибаз удаление внесенных данных, это единственное, до чего я додумался, как откатить БД)
        deleteDataFromCR();
    }
    /**
     * Не получилось положить данные в БД, так как данные не валидны (см. поле "month")
     * @throws IOException
     * @throws ServletException
     */
    @Test
    public void testPutDataFailed() throws IOException, ServletException {
        HttpSession sessionMock = mock(HttpSession.class);
        when(request.getSession(true)).thenReturn(sessionMock);

        User user1 = new User(3, "user2","user2", Role.SIMPLE_USER);
        when(sessionMock.getAttribute("user")).thenReturn(user1);

        String data = "{\"year\":2023,\"month\":sdfsdf,\"typeOfCounter\":{\"Hot Water\":300.0,"
                + "\"Heating\":300.0,\"Cold Water\":300.0}}";
        StringReader stringReader = new StringReader(data);
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        when(request.getReader()).thenReturn(bufferedReader);

        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        putDataServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        CounterReadingService service = new CounterReadingService();
        CounterReadingDTO dto = service.getLastUserInfo(user1);

        assertThat(dto).isNull();
    }
}
