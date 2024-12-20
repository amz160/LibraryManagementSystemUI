package com.library.librarymanagementsystemui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/librarydb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"; // Changed because I experienced an error when connecting to the database
    private static final String USER = "root";
    private static final String PASSWORD = "finalprojectpassword2024";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return connection;
    }

    public static void main(String[] args) {
        getConnection();
    }
}