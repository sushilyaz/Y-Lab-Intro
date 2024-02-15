//package org.example.in.servlets.userServlets;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import liquibase.Liquibase;
//import liquibase.database.Database;
//import liquibase.database.DatabaseFactory;
//import liquibase.database.jvm.JdbcConnection;
//import liquibase.exception.LiquibaseException;
//import liquibase.resource.ClassLoaderResourceAccessor;
//import org.example.repository.BaseRepository;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringReader;
//import java.io.StringWriter;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@Testcontainers
//public class UserControllerTest {
//    @Container
//    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
//            .withDatabaseName("test_suhoi")
//            .withUsername("test_suhoi")
//            .withPassword("test_qwerty");
//    private static Connection connection;
//    @InjectMocks
//    private SignUp signUpServlet;
//
//    @InjectMocks
//    private Auth authServlet;
//
//    @Mock
//    HttpServletResponse response;
//    @Mock
//    HttpServletRequest request;
//
//    @BeforeAll
//    static void setUp() throws SQLException, LiquibaseException {
//        postgresContainer.start();
//        connection = DriverManager.getConnection(postgresContainer.getJdbcUrl(),
//                postgresContainer.getUsername(), postgresContainer.getPassword());
//        Database database =
//                DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
//        Liquibase liquibase =
//                new Liquibase("db/changelog/changelogTest.xml", new ClassLoaderResourceAccessor(), database);
//        liquibase.update();
//        BaseRepository.setConnection(connection);
//        System.out.println("Migration is completed successfully");
//        connection.setAutoCommit(false);
//    }
//
//    @AfterAll
//    static void tearDown() throws SQLException {
//        connection.rollback();
//        connection.close();
//        postgresContainer.stop();
//    }
//
//    @BeforeEach
//    public void setUpEarlyTest() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    /**
//     * Успешная регистрация пользователя
//     * @throws IOException
//     */
//    @Test
//    public void testValidRegistration() throws IOException {
//
//        StringReader stringReader = new StringReader("{\"username\":\"testUser\",\"password\":\"testPassword\"}");
//        BufferedReader bufferedReader = new BufferedReader(stringReader);
//
//        when(request.getReader()).thenReturn(bufferedReader);
//        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
//        signUpServlet.doPost(request, response);
//        verify(response).setStatus(HttpServletResponse.SC_CREATED);
//    }
//
//    /**
//     * Пользователь не зарегистрирован (Миним. длина юзернейма = 4)
//     * @throws IOException
//     */
//    @Test
//    public void testNoValidRegistration() throws IOException {
//
//
//        StringReader stringReader = new StringReader("{\"username\":\"d\",\"password\":\"d\"}");
//        BufferedReader bufferedReader = new BufferedReader(stringReader);
//
//        when(request.getReader()).thenReturn(bufferedReader);
//        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
//        signUpServlet.doPost(request, response);
//        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
//    }
//
//    /**
//     * Успешная аутентификация (см. changelogTest.xml)
//     * @throws IOException
//     */
//    @Test
//    public void testAuthSuccess() throws IOException {
//
//        StringReader stringReader = new StringReader("{\"username\":\"admin\",\"password\":\"admin\"}");
//        BufferedReader bufferedReader = new BufferedReader(stringReader);
//
//        when(request.getReader()).thenReturn(bufferedReader);
//        HttpSession sessionMock = mock(HttpSession.class);
//        when(request.getSession(true)).thenReturn(sessionMock);
//        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
//        authServlet.doPost(request, response);
//        verify(response).setStatus(HttpServletResponse.SC_OK);
//    }
//    /**
//     * Фейловая аутентификация. Неправильный пароль (см. changelogTest.xml)
//     * @throws IOException
//     */
//    @Test
//    public void testAuthFailed() throws IOException {
//
//        StringReader stringReader = new StringReader("{\"username\":\"testUser\",\"password\":\"testUser\"}");
//        BufferedReader bufferedReader = new BufferedReader(stringReader);
//
//        when(request.getReader()).thenReturn(bufferedReader);
//        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
//        authServlet.doPost(request, response);
//        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//    }
//}
