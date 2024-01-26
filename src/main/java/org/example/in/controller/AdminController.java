package org.example.in.controller;

import org.example.dto.UserInfoDTO;
import org.example.model.User;
import org.example.repository.CounterReadingRepository;
import org.example.repository.UserRepository;
import org.example.service.AdminService;

import java.util.List;
import java.util.Scanner;

public class AdminController {
    private AdminService adminService;

    public AdminController() {
        adminService = new AdminService();
    }

    public void getActualCRUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        var counterReading = adminService.getUserInfo(username);
        if (counterReading != null) {
            System.out.println("Counter reading of user '" + username + "' for last month: " + counterReading);
        } else {
            System.out.println("User not found or user has no data");
        }
    }

    public void getCRUserForMonth() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter month: ");
        int monthCR = scanner.nextInt();
        System.out.print("Enter year: ");
        int yearCR = scanner.nextInt();
        var counterReading = adminService.getUserInfoForMonth(username, monthCR, yearCR);
        if (counterReading != null) {
            System.out.println("Counter reading of user '" + username + "' for '" + monthCR + "." + yearCR + "' : ");
            System.out.println(counterReading);
        } else {
            System.out.println("User not found or user has no data");
        }
    }
    public void getAllInfo() {
        List<UserInfoDTO> users = adminService.getAllUserInfo();
        if (!users.isEmpty()) {
            System.out.println(users);
        } else {
            System.out.println("Database has not users");
        }
    }


}
