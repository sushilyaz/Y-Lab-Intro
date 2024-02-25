package org.example;

import org.example.audit.config.EnableAudit;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAudit
public class YlabApplication {

    public static void main(String[] args) {
        SpringApplication.run(YlabApplication.class, args);
    }

}
