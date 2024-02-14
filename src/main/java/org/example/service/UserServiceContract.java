package org.example.service;

import org.example.dto.AuthDTO;
import org.example.dto.UserCreateDTO;
import org.example.dto.UserDTO;
import org.example.model.User;

public interface UserServiceContract {
    UserDTO registerUser(UserCreateDTO userCreateDTO);
    User authenticationUser (AuthDTO dto);
}
