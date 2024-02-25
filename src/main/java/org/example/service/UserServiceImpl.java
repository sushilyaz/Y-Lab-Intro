package org.example.service;

import org.example.dto.AuthDTO;
import org.example.dto.UserCreateDTO;
import org.example.dto.UserDTO;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserUtils currentUser;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, UserUtils currentUser) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.currentUser = currentUser;
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
    public User authenticationUser(AuthDTO dto) {
        Optional<User> existUser = userRepository.findByUsername(dto.getUsername());
        if (existUser.isPresent()) {
            User user = existUser.get();
            if (user.getPassword().equals(dto.getPassword())) {
                currentUser.setCurrentUser(user);
                return user;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
