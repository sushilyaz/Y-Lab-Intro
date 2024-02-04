package org.example.in.controller;


import org.example.dto.CounterReadingDTO;
import org.example.model.User;
import org.example.service.CounterReadingService;

import java.util.Map;
import java.util.Scanner;

/**
 * Класс контроллера показаний
 */
public class CounterReadingController {

    /**
     * Поле и инициализация сервиса
     */
    private CounterReadingService counterReadingService;

    public CounterReadingController() {
        counterReadingService = new CounterReadingService();
    }

    /**
     * Контроллер получения показателей аутентифицированного пользователя
     */
    public void getLatestData(User currentUser) {
        System.out.println();
        var lastCountingReading = counterReadingService.getLastUserInfo(currentUser);
        if (lastCountingReading != null) {
            System.out.println("\n" + lastCountingReading);
        } else {
            System.out.println("\nError! User '" + currentUser.getUsername() + "' has not data");
        }
    }

    /**
     * Контроллер внесения показателей аутентифицированного пользователя
     * Также присутствует валидация на вводимые значения
     */
    public void putData(User currentUser) {
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter month: ");
        int month = 0;
        try {
            month = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid month. Try again");
            putData(currentUser);
        }

        System.out.print("Enter year: ");
        int year = 0;
        try {
            year = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid month. Try again");
            putData(currentUser);
        }
        // Запрос на получение данных о существующих типах счетчиков и прогон по циклу
        var commonMap = counterReadingService.getTypeOfCounter();
        for (Map.Entry<String, Double> map : commonMap.entrySet()) {
            System.out.print("Enter readings for " + map.getKey() + ": ");
            double buf = 0;
            // валидация вводимых значений. Нельзя ввести отрицательное число и строчный символ
            try {
                buf = scanner.nextDouble();
                if (buf < 0) throw new Exception();
            } catch (Exception e) {
                System.out.println("Invalid value. Try again");
                putData(currentUser);
            }
            map.setValue(buf);
        }
        // преобразование введенных данных в удобный для работы тип
        CounterReadingDTO counterReadingDTO = new CounterReadingDTO(currentUser.getId(), year, month, commonMap);
        // Валидация. Вводимое число не должно быть меньше числа за предыдущий месяц
        var validCounterReading = counterReadingService.validationCounter(currentUser, counterReadingDTO);
        if (validCounterReading != null) {
            var counter = counterReadingService.submitCounterReading(currentUser, validCounterReading);
            if (counter != null) {
                System.out.println("\nData entered successfully!");
            } else {
                System.out.println("\nError! Data for this month already exists.");
            }
        } else {
            System.out.println("\nData no valid. Latest counter cant be less than current");
        }
    }

    /**
     * Контроллер получения последних внесенных показателей аутентифицированного пользователя,
     * также присутствует валидация на ввод месяца и обработка результатов выполнения сервиса
     */
    public void getDataForMonth(User currentUser) {
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter month: ");
        int month = 0;
        try {
            month = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid month. Try again");
            getDataForMonth(currentUser);
        }
        if (month < 1 || month > 12) {
            System.out.println("\nInvalid month. Please provide a valid month (1-12).");
            getDataForMonth(currentUser);
        }
        System.out.print("Enter year: ");
        int year = scanner.nextInt();
        var counterReadingForMonth = counterReadingService.getUserInfoForMonth(currentUser, month, year);
        if (counterReadingForMonth != null) {
            System.out.println("\n" + counterReadingForMonth);
        } else {
            System.out.println("\nError! User has not data for " + month + "." + year);
        }
    }

    /**
     * Контроллер получения внесенных за все время показателей аутентифицированного пользователя
     */
    public void getAllData(User currentUser) {
        var list = counterReadingService.getCRByUser(currentUser);
        if (list.isEmpty()) {
            System.out.println("\nUser has no data");
        } else {
            System.out.println("\nUser with username '" + currentUser.getUsername() + "' has data: ");
            for (var counter : list) {
                System.out.println("\n" + counter);
            }
            System.out.println();
        }
    }
}
