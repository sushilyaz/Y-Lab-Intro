package org.example.in.servlets;

import org.example.dto.AuthDTO;
import org.example.dto.UserCreateDTO;
import org.example.dto.UserDTO;
import org.example.model.User;
import org.example.service.UserService;
import org.example.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private UserService userService;
    private UserUtils userUtils;

    @Autowired
    public UserController(UserService userService, UserUtils userUtils) {
        this.userService = userService;
        this.userUtils = userUtils;
    }


    @PostMapping("/signup")
    public ResponseEntity<String> create(@RequestBody UserCreateDTO userCreateDTO) {
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
