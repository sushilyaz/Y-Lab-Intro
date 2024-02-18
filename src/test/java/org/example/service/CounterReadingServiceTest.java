package org.example.service;

import org.example.dto.CounterReadingDTO;
import org.example.in.controllers.TestData;
import org.example.mapper.CounterReadingMapper;
import org.example.model.User;
import org.example.repository.CounterReadingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CounterReadingServiceTest {
    @Mock
    private CounterReadingRepository counterReadingRepository;

    @Mock
    private CounterReadingMapper counterReadingMapper;

    @InjectMocks
    private CounterReadingServiceImpl counterReadingService;

    @Test
    void testGetLastUserInfo() {
        User user = new User();
        user.setId(3L);
        TestData testData = new TestData();
        when(counterReadingRepository.findLastCounterReading(any(Long.class))).thenReturn(testData.testCR());
        when(counterReadingMapper.entitiesToDTOs(any(List.class))).thenReturn(testData.initDTO());
        List<CounterReadingDTO> res = counterReadingService.getLastUserInfo(user);
        assertThat(res).isNotEmpty();
    }
}
