package org.example.in.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.AuthDTO;
import org.example.dto.UserCreateDTO;
import org.example.dto.UserDTO;
import org.example.model.User;
import org.example.service.UserService;
import org.example.utils.UserUtils;
import org.example.utils.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Errors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тесты для UserController
 */
public class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private UserUtils userUtils;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUpEach() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testCreateUserSuccess() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO("testUser", "testPassword");
        Mockito.doNothing().when(userValidator).validate(Mockito.any(Object.class), Mockito.any(Errors.class));
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
        Mockito.doNothing().when(userValidator).validate(Mockito.any(Object.class), Mockito.any(Errors.class));
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
