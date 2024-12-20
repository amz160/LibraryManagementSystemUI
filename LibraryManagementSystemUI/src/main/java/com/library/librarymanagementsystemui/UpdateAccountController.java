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

    /**
     * This method dynamically sets the current email and password for the logged-in user.
     * Called by MainController during navigation to the Update Account page.
     */
    public void setLoggedInUser(String email, String password) {
        currentEmailLabel.setText(email != null ? "Current Email: " + email : "Unavailable");
        currentPasswordLabel.setText(password != null ? "Current Password: " + password : "Unavailable");
    }

    public void initialize() {
        String currentEmail = SessionManager.getCurrentPatronEmail();
        String currentPassword = SessionManager.getCurrentPatronPassword();

        if (currentEmail != null) {
            currentEmailLabel.setText("Current Email: " + currentEmail);
        } else if (currentEmailLabel.getText().equals("Current Email: ")) {
            currentEmailLabel.setText("Unavailable");
        }

        if (currentPassword != null) {
            currentPasswordLabel.setText("Current Password: " + currentPassword);
        } else if (currentPasswordLabel.getText().equals("Current Password: ")) {
            currentPasswordLabel.setText("Unavailable");
        }
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
