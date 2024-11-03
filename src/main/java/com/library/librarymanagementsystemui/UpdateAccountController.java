package com.library.librarymanagementsystemui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateAccountController {

    @FXML
    private Label currentEmailLabel;

    @FXML
    private Label currentPasswordLabel;

    @FXML
    private TextField newEmailField;

    @FXML
    private PasswordField newPasswordField;

    public void initialize() {
        String currentEmail = SessionManager.getCurrentPatronEmail();
        String currentPassword = SessionManager.getCurrentPatronPassword();

        currentEmailLabel.setText("Current Email: " + (currentEmail != null ? currentEmail : "Not available"));
        currentPasswordLabel.setText("Current Password: " + (currentPassword != null ? currentPassword : "Not available"));
    }

    @FXML
    private void handleUpdateAccount() {
        String newEmail = newEmailField.getText().trim();
        String newPassword = newPasswordField.getText().trim();

        if (newEmail.isEmpty() && newPassword.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter at least one value to update.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Update the patron information
        String currentEmail = SessionManager.getCurrentPatronEmail();

        try (Connection connection = DatabaseConnection.getConnection()) {
            if (!newEmail.isEmpty()) {
                try (PreparedStatement stmt = connection.prepareStatement(
                        "UPDATE Patrons SET Email = ? WHERE Email = ?")) {
                    stmt.setString(1, newEmail);
                    stmt.setString(2, currentEmail);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        SessionManager.setCurrentPatronEmail(newEmail);
                        currentEmailLabel.setText("Current Email: " + newEmail);
                        System.out.println("Email updated successfully.");
                    }
                }
            }

            if (!newPassword.isEmpty()) {
                try (PreparedStatement stmt = connection.prepareStatement(
                        "UPDATE Patrons SET Password = ? WHERE Email = ?")) {
                    stmt.setString(1, newPassword);
                    stmt.setString(2, newEmail.isEmpty() ? currentEmail : newEmail);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        SessionManager.setCurrentPatronPassword(newPassword);
                        currentPasswordLabel.setText("Current Password: " + newPassword);
                        System.out.println("Password updated successfully.");
                    }
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Account information updated successfully.", ButtonType.OK);
            alert.showAndWait();

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update account information.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCancel() {
        currentEmailLabel.getScene().getWindow().hide();
    }
}