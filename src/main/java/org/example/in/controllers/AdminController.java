package org.example.in.controllers;


import org.example.dto.CounterReadingDTO;
import org.example.dto.UserInfoDTO;
import org.example.dto.adminDTO.ForMonthDTO;
import org.example.dto.adminDTO.NewKeyDTO;
import org.example.dto.adminDTO.UserNameDTO;
import org.example.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер действий администратора
 */
@RestController
public class AdminController {
    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/admin/add-new-key")
    public ResponseEntity<String> addNewKeys(@RequestBody NewKeyDTO newKeyDTO) {
        if (adminService.addNewKey(newKeyDTO.getNewKey())) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("New key added success");
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("New key added failed");
        }
    }

    @GetMapping("/admin/get-all-keys")
    public ResponseEntity<List<String>> getAllKeys() {
        List<String> listResult = adminService.getAllKey();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listResult);
    }

    @GetMapping("/admin/get-user-data")
    public ResponseEntity<List<CounterReadingDTO>> getAllUserData(@RequestBody UserNameDTO dto) {
        List<CounterReadingDTO> listResult = adminService.getCRByUser(dto.getUsername());
        if (listResult.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(listResult);
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(listResult);
        }
    }

    @GetMapping("/admin/get-all-users-data-readings")
    public ResponseEntity<List<UserInfoDTO>> getAllUsersDataReadings() {
        List<UserInfoDTO> listResult = adminService.getAllUserInfo();
        if (listResult.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(listResult);
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(listResult);
        }
    }

    @GetMapping("/admin/get-last-data-user")
    public ResponseEntity<List<CounterReadingDTO>> getLastDataUser(@RequestBody UserNameDTO dto) {
        List<CounterReadingDTO> listResult = adminService.getLastUserInfo(dto.getUsername());
        if (listResult.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(listResult);
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(listResult);
        }
    }
    @GetMapping("/admin/get-user-data-for-month")
    public ResponseEntity<List<CounterReadingDTO>> getUserDataForMonth(@RequestBody ForMonthDTO dto) {
        List<CounterReadingDTO> listResult = adminService.getUserInfoForMonth(dto.getUsername(), dto.getDate());
        if (listResult.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(listResult);
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(listResult);
        }
    }
}
