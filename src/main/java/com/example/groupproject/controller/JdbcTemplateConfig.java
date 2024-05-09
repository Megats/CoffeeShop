package com.example.groupproject.controller;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcTemplateConfig {

    public static void initializeDatabaseConnection() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coffeeshop","root","root");
            if(connection != null){
                System.out.println("Connected to the database");
            }else{
                System.out.println("Failed to connect to the database");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) {
        initializeDatabaseConnection();
    }

}