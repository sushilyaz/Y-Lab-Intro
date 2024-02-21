package org.example.in.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.model.User;
import org.example.service.CounterReadingService;
import org.example.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тесты для CounterReadingController
 */
public class CounterReadingControllerTest {
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private TestData testData = new TestData();

    @Mock
    private CounterReadingService counterReadingService;

    @Mock
    private UserUtils userUtils;

    @InjectMocks
    private CounterReadingController counterReadingController;

    @BeforeEach
    void setUpEach() {
        objectMapper.registerModule(new JavaTimeModule());
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(counterReadingController).build();
    }

    /**
     * Данные не найдены (возвращаю пустой лист)
     * @throws Exception
     */
    @Test
    void testGetLatestDataNotFound() throws Exception {
        when(userUtils.getCurrentUser()).thenReturn(testData.initTestUser());
        when(counterReadingService.getLastUserInfo(any(User.class))).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/latest-data-for-current-user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    /**
     * Данные найдены
     * @throws Exception
     */
    @Test
    void testGetLatestDataSuccess() throws Exception {
        when(userUtils.getCurrentUser()).thenReturn(testData.initTestUser());
        when(counterReadingService.getLastUserInfo(any(User.class))).thenReturn(testData.initDTO());
        mockMvc.perform(get("/latest-data-for-current-user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Проходит валидацию и удачный create
     * @throws Exception
     */
    @Test
    void testCreateCounterReadingSuccess() throws Exception {
        when(userUtils.getCurrentUser()).thenReturn(testData.initTestUser());
        when(counterReadingService.validationCounter(any(User.class), any(List.class))).thenReturn(testData.initCreateDTO());
        when(counterReadingService.submitCounterReading(any(User.class), any(List.class))).thenReturn(testData.initDTO());
        mockMvc.perform(post("/put-counter-reading")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testData.initDTO())))
                .andExpect(status().isCreated());
    }
    /**
     * Не проходит валидацию и удачный create
     * @throws Exception
     */
    @Test
    void testCreateCounterReadingFailedNoValid() throws Exception {
        when(userUtils.getCurrentUser()).thenReturn(testData.initTestUser());
        when(counterReadingService.validationCounter(any(User.class), any(List.class))).thenReturn(Collections.emptyList());
        when(counterReadingService.submitCounterReading(any(User.class), any(List.class))).thenReturn(testData.initDTO());
        mockMvc.perform(post("/put-counter-reading")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testData.initDTO())))
                .andExpect(status().isBadRequest());
    }
    /**
     * Проходит валидацию и неудачный create
     * @throws Exception
     */
    @Test
    void testCreateCounterReadingFailed() throws Exception {
        when(userUtils.getCurrentUser()).thenReturn(testData.initTestUser());
        when(counterReadingService.validationCounter(any(User.class), any(List.class))).thenReturn(testData.initCreateDTO());
        when(counterReadingService.submitCounterReading(any(User.class), any(List.class))).thenReturn(Collections.emptyList());
        mockMvc.perform(post("/put-counter-reading")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testData.initDTO())))
                .andExpect(status().isConflict());
    }

    /**
     * Данные найдены
     * @throws Exception
     */
    @Test
    void testGetAllDataSuccess() throws Exception {
        when(userUtils.getCurrentUser()).thenReturn(testData.initTestUser());
        when(counterReadingService.getCRByUser(any(User.class))).thenReturn(testData.initDTO());
        mockMvc.perform(get("/get-all-data")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Данные не найдены
     * @throws Exception
     */
    @Test
    void testGetAllDataFailed() throws Exception {
        when(userUtils.getCurrentUser()).thenReturn(testData.initTestUser());
        when(counterReadingService.getCRByUser(any(User.class))).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/get-all-data")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
