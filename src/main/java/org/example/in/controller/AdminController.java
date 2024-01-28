package org.example.in.controller;

import org.example.dto.UserInfoDTO;
import org.example.service.AdminService;

import java.util.List;
import java.util.Scanner;

public class AdminController {
    private AdminService adminService;

    public AdminController() {
        adminService = new AdminService();
    }

    /**
     * Контроллер вывода аудита (только для администратора)
     */
    public void getAudit() {
        System.out.println();
        System.out.println(adminService.getLogs());
    }

    public void addNewReading() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter new type of reading: ");
        String newKey = scanner.nextLine();
        if (adminService.addNewKey(newKey)) {
            System.out.println("New type of reading: " + newKey + "adding success!");
        } else {
            System.out.println("This key alrady exist");
        }
    }
    public void viewAllKey() {
        System.out.println(adminService.getAllKey());
    }
    /**
     * Контроллер получения последних внесенных показаний счетчика определенного пользователя (по username)
     */
    public void getActualCRUser() {
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        var counterReading = adminService.getLastUserInfo(username);
        if (counterReading != null) {
            System.out.println("Counter reading of user '" + username + "' for last month: " + counterReading);
        } else {
            System.out.println("User not found or user has no data");
        }
    }

    /**
     * Контроллер получения показаний счетчика за определенный месяц определенного пользователя (по username)
     */
    public void getCRUserForMonth() {
        System.out.println();
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

    /**
     * Контроллер получения показаний всех пользователей
     */
    public void getAllInfo() {
        System.out.println();
        List<UserInfoDTO> users = adminService.getAllUserInfo();
        if (!users.isEmpty()) {
            System.out.println(users);
        } else {
            System.out.println("Database has not users");
        }
    }


}
