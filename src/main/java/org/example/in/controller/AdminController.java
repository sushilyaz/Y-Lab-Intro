package org.example.in.controller;

import org.example.dto.UserInfoDTO;
import org.example.model.User;
import org.example.service.AdminService;

import java.util.List;
import java.util.Scanner;

/**
 * Контроллер админа
 */
public class AdminController {
    /**
     * Поле
     */
    private AdminService adminService;

    /**
     * Конструктор класса
     */
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

    /**
     * Добавление новых показаний, вызывает булев метод addNewKey. Если тру - значит добавляем,
     * если фолс - значит такой ключ в мапе уже существует
     */
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

    /**
     * Просмотр всех показаний
     */
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
        var counterReading = adminService.getLastUserInfo(new User(username));
        if (counterReading != null) {
            System.out.println("\nCounter reading of user '" + username + "' for last month: " + counterReading);
        } else {
            System.out.println("\nUser not found or user has no data");
        }
    }

    public void getCRByUser() {
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        var counterReading = adminService.getCRByUser(new User(username));
        if (!counterReading.isEmpty()) {
            System.out.println("\nCounter reading of user '" + username + "' for all time: " + counterReading);
        } else {
            System.out.println("\nUser not found or user has no data");
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
        int monthCR = 0;
        try {
            monthCR = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("\nInvalid month. Try again");
            getCRUserForMonth();
        }
        System.out.print("Enter year: ");
        int yearCR = 0;
        try {
            yearCR = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("\nInvalid year. Try again");
            getCRUserForMonth();
        }

        var counterReading = adminService.getUserInfoForMonth(new User(username), monthCR, yearCR);
        if (counterReading != null) {
            System.out.println("\nCounter reading of user '" + username + "' for '" + monthCR + "." + yearCR + "' : ");
            System.out.println(counterReading);
        } else {
            System.out.println("\nUser not found or user has no data");
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
            System.out.println("\nDatabase has not users");
        }
    }
}
