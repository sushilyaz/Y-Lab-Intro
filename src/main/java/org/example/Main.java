package org.example;

import org.example.repository.BaseRepository;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to housing and communal services !");
        // Инициализация ликибаз и установка соединения с БД
        BaseRepository.initializeConnection();
        // Говорю главному потоку подождать, чтобы все логи ликибаза вывелись и не перекрыли главное меню приложения
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Вызов главного меню приложения
    }
}
