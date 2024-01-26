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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.PrimitiveIterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {
    private UserRepository userRepository = new UserRepository();
    private UserService userService = new UserService(userRepository);
    private CounterReadingRepository counterReadingRepository = new CounterReadingRepository();
    private CounterReadingService counterReadingService = new CounterReadingService(counterReadingRepository);
    private AdminService adminService = new AdminService(userRepository, counterReadingRepository);

    @BeforeEach
    public void setUp() {
        User user1 = new User(2, "lol", "lol", false);
        userRepository.save(user1);
        User user2 = new User(3, "kek", "kek", false);
        userRepository.save(user2);
        TypeOfCounter typeOfCounter = new TypeOfCounter(300.1, 322.5, 268.4);
        CounterReading counterReading1 = new CounterReading(2020, 4, typeOfCounter);
        counterReading1.setUserId(2);
        counterReadingRepository.submit(counterReading1);
        CounterReading counterReading2 = new CounterReading(2021, 6, typeOfCounter);
        counterReading2.setUserId(2);
        counterReadingRepository.submit(counterReading2);
        CounterReading counterReading3 = new CounterReading(2024, 1, typeOfCounter);
        counterReading3.setUserId(3);
        counterReadingRepository.submit(counterReading3);
    }

    @Test
    public void testRegisterFailed() {
        String username = "lol";
        String password = "lol";
        int sizeBefore = userRepository.getUsers().size();
        userService.registerUser(username, password);
        int sizeAfter = userRepository.getUsers().size();
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    public void testRegisterSuccess() {
        String username = "user1";
        String password = "user1";
        User userCheck = new User(4, "user1", "user1", false);
        var users = userRepository.getUsers();
        userService.registerUser(username, password);
        assertThat(users).contains(userCheck);
    }

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

    @Test
    public void testLoginSuccess() {
        String username = "lol";
        String password = "lol";
        User user = userService.authenticationUser(username, password);
        assertThat(user).isNotNull();
    }

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
    @Test
    public void testAdmin() {
        var list = adminService.getAllUserInfo();
        UserInfoDTO userInfoCheck = new UserInfoDTO("lol", 2020, 4, 300.1, 322.5, 268.4);
        assertThat(list).contains(userInfoCheck);
    }
}
