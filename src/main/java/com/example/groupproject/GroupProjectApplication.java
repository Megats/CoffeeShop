package com.example.groupproject;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootApplication
public class GroupProjectApplication {

    public static void main(String[] args) {

        SpringApplication.run(GroupProjectApplication.class, args);


    }
    @Bean
    public ApplicationRunner initializeDatabaseConnection() {
        return args -> {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coffeedb", "root", "Arsenal2001-");
                if (connection != null) {
                    System.out.println("Connected to the database");
                    // Optionally, you can perform additional initialization here.
                }
            } catch (SQLException ex) {
                System.err.println("Cannot connect to the database: " + ex.getMessage());
            }
        };
    }

}
