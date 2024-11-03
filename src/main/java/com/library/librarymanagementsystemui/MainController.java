package com.library.librarymanagementsystemui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button createPatronButton;

    @FXML
    private Button addBookButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button manageBooksButton;

    @FXML
    private Button searchBooksButton;

    @FXML
    private Button borrowedBooksButton;

    @FXML
    private Button updateAccountButton;

    @FXML
    private Button wishlistButton;

    public void initialize() {
        // Hide all buttons initially except login and create patron button
        loginButton.setVisible(true);
        createPatronButton.setVisible(true);
        addBookButton.setVisible(false);
        logoutButton.setVisible(false);
        manageBooksButton.setVisible(false);
        searchBooksButton.setVisible(false);
        borrowedBooksButton.setVisible(false);
        updateAccountButton.setVisible(false);
        wishlistButton.setVisible(false);
    }

    public void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM Patrons WHERE Email = ? AND Password = ?")) {
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                boolean isLibrarian = rs.getInt("isLibrarian") == 1;
                System.out.println("Login successful for: " + email);

                // Store patron email, password, and ID in session manager
                SessionManager.setCurrentPatronEmail(email);
                SessionManager.setCurrentPatronPassword(password);
                SessionManager.setCurrentPatronID(rs.getInt("PatronID"));

                // Hide login button after login
                loginButton.setVisible(false);

                // Showing appropriate buttons based on user
                if (isLibrarian) {
                    addBookButton.setVisible(true);
                    manageBooksButton.setVisible(true);
                } else {
                    searchBooksButton.setVisible(true);
                    borrowedBooksButton.setVisible(true);
                    updateAccountButton.setVisible(true);
                    wishlistButton.setVisible(true);
                }
                logoutButton.setVisible(true);
            } else {
                System.out.println("Invalid email or password.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void handleLogout() {
        // Clear the login fields
        emailField.clear();
        passwordField.clear();

        // Clear the session
        SessionManager.clearSession();

        // Reset visibility of buttons
        loginButton.setVisible(true);
        createPatronButton.setVisible(true);
        addBookButton.setVisible(false);
        logoutButton.setVisible(false);
        manageBooksButton.setVisible(false);
        searchBooksButton.setVisible(false);
        borrowedBooksButton.setVisible(false);
        updateAccountButton.setVisible(false);
        wishlistButton.setVisible(false);
        System.out.println("User has logged out.");
    }

    // Opens the Create New Patron page
    public void openCreatePatronPage() {
        openPage("/com/library/librarymanagementsystemui/CreatePatron.fxml", "Create New Patron");
    }

    // Opens the Add New Book page
    public void openAddBookPage() {
        openPage("/com/library/librarymanagementsystemui/AddBook.fxml", "Add New Book");
    }

    // Opens the Manage Books page
    public void openManageBooksPage() {
        openPage("/com/library/librarymanagementsystemui/ManageBooksPage.fxml", "Manage Books");
    }

    // Opens the Search Books page for patrons
    public void openPatronSearchPage() {
        openPage("/com/library/librarymanagementsystemui/PatronSearch.fxml", "Search Books");
    }

    // Opens the Borrowed Books page for patrons
    public void openBorrowedBooksPage() {
        openPage("/com/library/librarymanagementsystemui/ReturnBooksPage.fxml", "Borrowed Books");
    }

    // Opens the Update Account Information page for patrons
    public void openUpdateAccountPage() {
        openPage("/com/library/librarymanagementsystemui/UpdateAccount.fxml", "Update Account Information");
    }

    // Opens the Wishlist page for patrons
    public void openWishlistPage() {
        openPage("/com/library/librarymanagementsystemui/WishlistPage.fxml", "Wishlist");
    }

    private void openPage(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}