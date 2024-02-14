package org.example.service;

import org.example.dto.AuthDTO;
import org.example.dto.UserCreateDTO;
import org.example.dto.UserDTO;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.repository.UserActionRepository;
import org.example.repository.UserRepository;

import java.util.Optional;

public class UserService implements UserServiceContract{
    private UserRepository userRepository;
    private UserActionRepository userActionRepository;
    private UserMapper userMapper = UserMapper.INSTANCE;

    public UserService() {
        this.userActionRepository = UserActionRepository.getInstance();
        this.userRepository = UserRepository.getInstance();
    }

    public UserDTO registerUser(UserCreateDTO userCreateDTO) {
        String username = userCreateDTO.getUsername();
        Optional<User> existUser = userRepository.findByUsername(username);
        if (existUser.isPresent()) {
            return null;
        } else {
            User newUser = userMapper.map(userCreateDTO);
            newUser.setRoleFromString("SIMPLE_USER");
            userRepository.save(newUser);
            return userMapper.map(newUser);
        }
    }

    public User authenticationUser (AuthDTO dto) {
        Optional<User> existUser = userRepository.findByUsername(dto.getUsername());
        if (existUser.isPresent()) {
            User user = existUser.get();
            if (user.getPassword().equals(dto.getPassword())) {
                return user;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
