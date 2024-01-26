package org.example;

import org.example.dto.UserInfoDTO;
import org.example.model.CounterReading;
import org.example.model.TypeOfCounter;
import org.example.model.User;
import org.example.repository.CounterReadingRepository;
import org.example.repository.UserRepository;
import org.example.service.AdminService;
import org.example.service.CounterReadingService;
import org.example.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.PrimitiveIterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Тестировал только пограничные значения
 */
public class MainTest {
    private UserRepository userRepository;
    private UserService userService;
    private CounterReadingRepository counterReadingRepository;
    private CounterReadingService counterReadingService;
    private AdminService adminService;

    /**
     * Инициализация данных перед каждым тестом
     */
    @BeforeEach
    public void setUp() {
        userRepository = UserRepository.getInstance();
        userService = new UserService();
        counterReadingRepository = CounterReadingRepository.getInstance();
        counterReadingService = new CounterReadingService();
        adminService = new AdminService();

        User user1 = new User(2, "lol", "lol", false);
        userRepository.save(user1);
        User user2 = new User(3, "kek", "kek", false);
        userRepository.save(user2);
        TypeOfCounter typeOfCounter = new TypeOfCounter(300.1, 322.5, 268.4);
        CounterReading counterReading1 = new CounterReading(2020, 4, typeOfCounter);
        counterReading1.setUserId(2);
        counterReadingService.submitCounterReading(user1, counterReading1);
        CounterReading counterReading2 = new CounterReading(2021, 6, typeOfCounter);
        counterReading2.setUserId(2);
        counterReadingService.submitCounterReading(user1, counterReading2);
        CounterReading counterReading3 = new CounterReading(2024, 1, typeOfCounter);
        counterReading3.setUserId(3);
        counterReadingService.submitCounterReading(user2, counterReading3);
    }
    /**
     * Откат репозитория
     */
    @AfterEach
    public void clear() {
        UserRepository.reset();
        CounterReadingRepository.reset();
    }
    /**
     * Неудачная регистрация (пользователь уже существует)
     */
    @Test
    public void testRegisterFailed() {
        String username = "lol";
        String password = "lol";
        int sizeBefore = userRepository.getUsers().size();
        userService.registerUser(username, password);
        int sizeAfter = userRepository.getUsers().size();
        assertEquals(sizeBefore, sizeAfter);
    }
    /**
     * Удачная регистрация
     */
    @Test
    public void testRegisterSuccess() {
        String username = "user1";
        String password = "user1";
        User userCheck = new User(4, username, password, false);
        int sizeBefore = userRepository.getUsers().size();
        userService.registerUser(username, password);
        int sizeAfter = userRepository.getUsers().size();
        var users = userRepository.getUsers();
        assertThat(users).contains(userCheck);
        assertNotEquals(sizeBefore, sizeAfter);
    }

    /**
     * Неудачная аутентификация (неверный пароль, несуществующий пользователь)
     */
    @Test
    public void testLoginFailed() {
        String username1 = "lol";
        String password1 = "kek";
        User user1 = userService.authenticationUser(username1, password1);
        assertThat(user1).isNull();
        String username2 = "wow";
        String password2 = "wow";
        User user2 = userService.authenticationUser(username2, password2);
        assertThat(user2).isNull();
    }

    /**
     * Удачный логин
     */
    @Test
    public void testLoginSuccess() {
        String username = "lol";
        String password = "lol";
        User user = userService.authenticationUser(username, password);
        assertThat(user).isNotNull();
    }

    /**
     * Удачное внесение данных счетчика пользователя
     */
    @Test
    public void testSubmitCounterReadingSuccess() {
        User user = userRepository.findByUsername("lol").get();
        TypeOfCounter typeOfCounter = new TypeOfCounter(300.1, 322.5, 268.4);
        CounterReading counterReading1 = new CounterReading(2019, 4, typeOfCounter);
        counterReading1.setUserId(user.getId());
        int sizeBefore = counterReadingRepository.getCounterReadings().size();
        counterReadingService.submitCounterReading(user, counterReading1);
        int sizeAfter = counterReadingRepository.getCounterReadings().size();
        var counterReading2 = counterReadingRepository.findCounterReadingForMonth(user.getId(), 4, 2019);

        assertEquals(counterReading2, counterReading1);
        assertNotEquals(sizeAfter, sizeBefore);
    }

    /**
     * Неудачное внесение данных пользователя (Данные за этот месяц уже существуют)
     */
    @Test
    public void testSubmitCounterReadingFailed() {
        User user = userRepository.findByUsername("lol").get();
        TypeOfCounter typeOfCounter = new TypeOfCounter(300.1, 322.5, 268.4);
        CounterReading counterReading1 = new CounterReading(2021, 6, typeOfCounter);
        counterReading1.setUserId(user.getId());
        int sizeBefore = counterReadingRepository.getCounterReadings().size();
        counterReadingService.submitCounterReading(user, counterReading1);
        int sizeAfter = counterReadingRepository.getCounterReadings().size();

        assertEquals(sizeAfter, sizeBefore);
    }

    /**
     * Валидация вносимых данных - Удача
     */
    @Test
    public void testValidationCounterSuccess() {
        User user = userRepository.findByUsername("lol").get();
        TypeOfCounter typeOfCounter = new TypeOfCounter(900.1, 322.5, 268.4);
        CounterReading counterReading1 = new CounterReading(2022, 9, typeOfCounter);
        counterReading1.setUserId(user.getId());
        var counter = counterReadingService.validationCounter(user, counterReading1);
        assertThat(counter).isNotNull();
    }

    /**
     * Неудачная валидация. За текущий месяц значения hotWater меньше, чем у предыдущего месяца)
     */
    @Test
    public void testValidationCounterFailed() {
        User user = userRepository.findByUsername("lol").get();
        TypeOfCounter typeOfCounter = new TypeOfCounter(200, 322.5, 268.4);
        CounterReading counterReading1 = new CounterReading(2022, 9, typeOfCounter);
        counterReading1.setUserId(user.getId());
        var counter = counterReadingService.validationCounter(user, counterReading1);
        assertThat(counter).isNull();
    }

    /**
     * Удачное получение последних внесенных показателей
     */
    @Test
    public void testGetLatestCounterReadingSuccess() {
        User user = userRepository.findByUsername("lol").get();
        TypeOfCounter typeOfCounter = new TypeOfCounter(300.1, 322.5, 268.4);
        CounterReading counterReading1 = new CounterReading(2021, 6, typeOfCounter);
        counterReading1.setUserId(user.getId());
        CounterReading counterReading2 = counterReadingService.getLatestCounterReading(user);
        assertEquals(counterReading1, counterReading2);
    }

    /**
     * Удачное получение последних внесенных показателей
     * Здесь counterReading1 является не последним показателем
     */
    @Test
    public void testGetLatestCounterReadingFailed() {
        User user = userRepository.findByUsername("lol").get();
        TypeOfCounter typeOfCounter = new TypeOfCounter(300.1, 322.5, 268.4);
        CounterReading counterReading1 = new CounterReading(2020, 4, typeOfCounter);
        counterReading1.setUserId(user.getId());
        CounterReading counterReading2 = counterReadingService.getLatestCounterReading(user);
        assertNotEquals(counterReading1, counterReading2);
    }
    /**
     * Удачное получение показателей за конкретный месяц
     */
    @Test
    public void testGetCounterReadingForMonthSuccess() {
        User user = userRepository.findByUsername("lol").get();
        var counterReading = counterReadingService.getCounterReadingForMonth(user, 4, 2020);
        assertThat(counterReading).isNotNull();
        assertThat(counterReading.getMonth()).isEqualTo(4);
        assertThat(counterReading.getYear()).isEqualTo(2020);
    }
    /**
     * Неудачное получение показателей за конкретный месяц
     * У пользователя с данным username нет данных за этот месяц
     */
    @Test
    public void testGetCounterReadingForMonthFailed() {
        User user = userRepository.findByUsername("lol").get();
        var counterReading = counterReadingService.getCounterReadingForMonth(user, 1, 2024);
        assertThat(counterReading).isNull();
    }
    /**
     * Удачное получение всех показателей пользователя
     */
    @Test
    public void testGetAllCounterReadingForUserSuccess() {
        User user = userRepository.findByUsername("lol").get();
        var counterReading = counterReadingService.getAllCounterReadingForUser(user);

        TypeOfCounter typeOfCounter = new TypeOfCounter(300.1, 322.5, 268.4);
        CounterReading counterReading1 = new CounterReading(2020, 4, typeOfCounter);
        counterReading1.setUserId(2);
        CounterReading counterReading2 = new CounterReading(2021, 6, typeOfCounter);
        counterReading2.setUserId(2);

        assertThat(counterReading.size()).isEqualTo(2);
        assertThat(counterReading).contains(counterReading1);
        assertThat(counterReading).contains(counterReading2);
    }
    /**
     * Неудачное получение показателей
     */
    @Test
    public void testGetAllCounterReadingForUserFailed() {
        User user = userRepository.findByUsername("lol").get();
        var counterReading1 = counterReadingService.getAllCounterReadingForUser(user);

        TypeOfCounter typeOfCounter = new TypeOfCounter(300.1, 322.5, 268.4);
        CounterReading counterReading2 = new CounterReading(2024, 1, typeOfCounter);
        counterReading2.setUserId(3);

        assertFalse(counterReading1.contains(counterReading2));
    }
    /**
     * Удачное получение всей информации пользователей для администратора
     */
    @Test
    public void testAdmin() {
        var list = adminService.getAllUserInfo();
        UserInfoDTO userInfoCheck = new UserInfoDTO("lol", 2020, 4, 300.1, 322.5, 268.4);
        assertThat(list).contains(userInfoCheck);
    }
}
