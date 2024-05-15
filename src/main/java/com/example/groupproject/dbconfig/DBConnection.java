package com.example.groupproject.dbconfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static void main(String[] args) throws SQLException {
        String jdbcUrl = DatabaseConfig.JDBC_URL;
        String username = DatabaseConfig.USERNAME;
        String password = DatabaseConfig.PASSWORD;

        Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

        if(connection != null){
            System.out.println("Connection successfully!");
        } else {
            System.out.println("Connection failed!");
        }

        // Close the connection
        connection.close();
    }

}
