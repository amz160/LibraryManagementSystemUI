package com.library.librarymanagementsystemui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatronDAO {

    // Add a new patron to the database
    public boolean addPatron(Patron patron) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO Patrons (Name, Email, Password, isLibrarian) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, patron.getName());
            statement.setString(2, patron.getEmail());
            statement.setString(3, patron.getPassword());
            statement.setInt(4, patron.getIsLibrarian() ? 1 : 0); // Convert boolean to int
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding patron: " + e.getMessage());
            return false;
        }
    }

    // Retrieve a patron's details from the database
    public Patron getPatron(int patronID) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM Patrons WHERE PatronID = ?")) {
            statement.setInt(1, patronID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Patron(
                        resultSet.getInt("PatronID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Email"),
                        resultSet.getString("Password"),
                        resultSet.getInt("isLibrarian") == 1 // Convert int to boolean
                );
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving patron: " + e.getMessage());
        }
        return null;
    }

    // Update a patron's details in the database
    public boolean updatePatron(Patron patron) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Patrons SET Name = ?, Email = ?, Password = ?, isLibrarian = ? WHERE PatronID = ?")) {
            statement.setString(1, patron.getName());
            statement.setString(2, patron.getEmail());
            statement.setString(3, patron.getPassword());
            statement.setInt(4, patron.getIsLibrarian() ? 1 : 0); // Convert boolean to int
            statement.setInt(5, patron.getPatronID());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating patron: " + e.getMessage());
            return false;
        }
    }

    // Delete a patron from the database
    public boolean deletePatron(int patronID) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM Patrons WHERE PatronID = ?")) {
            statement.setInt(1, patronID);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting patron: " + e.getMessage());
            return false;
        }
    }
}
