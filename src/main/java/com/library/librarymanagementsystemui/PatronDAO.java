package com.library.librarymanagementsystemui;

import java.sql.*;

public class PatronDAO {

    public boolean addPatron(Patron patron) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO Patrons (Name, Email) VALUES (?, ?)")) {
            statement.setString(1, patron.getName());
            statement.setString(2, patron.getEmail());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding patron: " + e.getMessage());
            return false;
        }
    }

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
                        resultSet.getString("Email"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving patron: " + e.getMessage());
        }
        return null;
    }
    public boolean createPatron(String name, String email, String password) {
        String sql = "INSERT INTO Patrons (Name, Email, Password, AccountCreated) VALUES (?, ?, ?, NOW())";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error creating patron: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePatron(Patron patron) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Patrons SET Name = ?, Email = ? WHERE PatronID = ?")) {
            statement.setString(1, patron.getName());
            statement.setString(2, patron.getEmail());
            statement.setInt(3, patron.getUserId());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating patron: " + e.getMessage());
            return false;
        }
    }

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