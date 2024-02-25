package org.example.in.controllers;

import org.example.dto.AuthDTO;
import org.example.dto.UserCreateDTO;
import org.example.dto.UserDTO;
import org.example.exception.InvalidData;
import org.example.model.User;
import org.example.service.UserService;
import org.example.utils.UserUtils;
import org.example.utils.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * Контроллер для регистрации\авторизации пользователя
 */
@RestController
public class UserController {
    private UserService userService;
    private UserValidator userValidator;
    private UserUtils userUtils;

    @Autowired
    public UserController(UserService userService, UserUtils userUtils, UserValidator userValidator) {
        this.userService = userService;
        this.userUtils = userUtils;
        this.userValidator = userValidator;
    }


    @PostMapping("/signup")
    public ResponseEntity<String> create(@RequestBody UserCreateDTO userCreateDTO, BindingResult result) {
        userValidator.validate(userCreateDTO, result);
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(";\n"));
            throw new InvalidData(errors);
        }
        UserDTO userDTO = userService.registerUser(userCreateDTO);
        if (userDTO != null) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("User registered successfully!");
        } else {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("User registered failed!");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> auth(@RequestBody AuthDTO authDTO) {
        User user = userService.authenticationUser(authDTO);
        if (user != null) {
            userUtils.setCurrentUser(user);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("User auth successfully!");
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Incorrect username or password");
        }
    }
}
