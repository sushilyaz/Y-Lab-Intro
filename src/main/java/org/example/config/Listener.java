package org.example.config;

import liquibase.exception.LiquibaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.example.config.DatabaseMigration.runLiquibaseMigrations;

@Component
public class Listener implements ApplicationListener<ContextRefreshedEvent> {
    /**
     * При запуске сервера данный класс прослушивается и инициализируется контекст
     * @param sce
     */
    private MyConnectionPool myConnectionPool;

    @Autowired
    public Listener(MyConnectionPool myConnectionPool) {
        this.myConnectionPool = myConnectionPool;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            Connection connection = myConnectionPool.getConnection();
            runLiquibaseMigrations(connection);
            myConnectionPool.returnConnection(connection);
        } catch (SQLException | LiquibaseException | IOException e) {
            System.out.println("Failed migration or connection with: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
