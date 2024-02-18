package org.example.service;

import org.example.dto.UserCreateDTO;
import org.example.dto.UserDTO;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;
    @Test
    void testRegisterNewUser() {
        UserCreateDTO userCreateDTO = new UserCreateDTO("newUser", "password");
        UserDTO newUser = new UserDTO();
        newUser.setUsername("newUser");
        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(userMapper.map(userCreateDTO)).thenReturn(new User());
        when(userMapper.map(any(User.class))).thenReturn(newUser);
        UserDTO result = userService.registerUser(userCreateDTO);
        assertEquals(newUser.getUsername(), result.getUsername());
    }
}
