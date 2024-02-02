package org.example;

import org.example.in.Engine;
import org.example.repository.BaseRepository;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to housing and communal services !");
        BaseRepository.initializeConnection();
        Engine.start();
    }
}
