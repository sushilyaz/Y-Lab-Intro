//
//package org.example;
//
//
///**
// * Тестировал только пограничные значения
// */
//@Testcontainers
//public class UserServiceTest {
//    /**
//     * Создание тестового контейнера с подключением
//     */
//    @Container
//    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
//            .withDatabaseName("test_suhoi")
//            .withUsername("test_suhoi")
//            .withPassword("test_qwerty");
//    private Connection connection;
//    UserService userService = new UserService();
//    UserRepository userRepository = UserRepository.getInstance();
//    /**
//     *  Установка соединение с контейнером, передача соединения BaseRepository и выполнение скриптов миграции
//     */
//    @BeforeEach
//    void setUp() throws SQLException, LiquibaseException {
//        postgresContainer.start();
//        connection = DriverManager.getConnection(postgresContainer.getJdbcUrl(),
//                postgresContainer.getUsername(), postgresContainer.getPassword());
//        Database database =
//                DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
//        Liquibase liquibase =
//                new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
//        liquibase.update();
//        BaseRepository.connection = connection;
//        System.out.println("Migration is completed successfully");
//        connection.setAutoCommit(false);
//    }
//    /**
//     *  Инициализация
//     */
//    User initUsers() {
//        String username = "testUser";
//        String password = "testUser";
//        User user = userService.registerUser(username, password);
//        return user;
//    }
//    /**
//     *  Закрытие соединения
//     */
//    @AfterEach
//    void tearDown() throws SQLException {
//        connection.rollback();
//        connection.close();
//        postgresContainer.stop();
//    }
//    /**
//     *  Успешная регистрация
//     */
//    @Test
//    void registerSuccess() {
//        User user = initUsers();
//        assertThat(user).isNotNull();
//        var check = userRepository.findByUsername("testUser").get();
//        assertThat(user).isEqualTo(check);
//    }
//    /**
//     *  Неуспешная регистрация (дублирование)
//     */
//    @Test
//    void registerFailed() {
//        User testUser = initUsers();
//        User user = userService.registerUser("testUser", "testUser");
//        assertThat(user).isNull();
//    }
//    /**
//     *  Успешная авторизация
//     */
//    @Test
//    void authSuccess() {
//        User user = initUsers();
//        var check = userService.authenticationUser("testUser", "testUser");
//        assertThat(check).isNotNull();
//        assertThat(check).isEqualTo(user);
//    }
//    /**
//     *  Неуспешная авторизация. Неправильный пароль
//     */
//    @Test
//    void authFailed() {
//        initUsers();
//        var check = userService.authenticationUser("testUser", "failedPassword");
//        assertThat(check).isNull();
//    }
//}
