package com.library.librarymanagementsystemui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainController {

    @FXML
    private StackPane contentPane;

    @FXML
    private Button loginButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button createPatronButton;

    @FXML
    private Button addBookButton;

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

    @FXML
    private Button reservationsButton;

    @FXML
    private Button viewTransactionsButton;

    @FXML
    private Button managePatronsButton;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private String loggedInEmail; // To store the logged-in user's email
    private String loggedInPassword; // To store the logged-in user's password

    public void initialize() {
        showHomePage();
        loginButton.setVisible(true);
        createPatronButton.setVisible(true);
        logoutButton.setVisible(false);
        addBookButton.setVisible(false);
        manageBooksButton.setVisible(false);
        searchBooksButton.setVisible(false);
        borrowedBooksButton.setVisible(false);
        updateAccountButton.setVisible(false);
        wishlistButton.setVisible(false);
        reservationsButton.setVisible(false);
        viewTransactionsButton.setVisible(false);
        managePatronsButton.setVisible(false);
    }

    private void loadFXMLIntoContentPane(String fxmlPath) {
        try {
            Node page = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showHomePage() {
        System.out.println("Navigating to Home page.");
        contentPane.getChildren().clear(); // Clear the content pane
    }

    @FXML
    private void showUpdateAccountPage() {
        System.out.println("Navigating to Update Account Page.");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/librarymanagementsystemui/UpdateAccount.fxml"));
            Node page = loader.load();

            // Pass the logged-in user's details to UpdateAccountController
            UpdateAccountController controller = loader.getController();
            controller.setLoggedInUser(loggedInEmail, loggedInPassword);

            contentPane.getChildren().clear();
            contentPane.getChildren().add(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showBorrowedBooksPage() {
        System.out.println("Navigating to Borrowed Books Page.");
        loadFXMLIntoContentPane("/com/library/librarymanagementsystemui/ReturnBooksPage.fxml");
    }

    @FXML
    private void showWishlistPage() {
        System.out.println("Navigating to Wishlist Page.");
        loadFXMLIntoContentPane("/com/library/librarymanagementsystemui/WishlistPage.fxml");
    }

    @FXML
    private void showReservationsPage() {
        System.out.println("Navigating to Reservations Page.");
        loadFXMLIntoContentPane("/com/library/librarymanagementsystemui/PatronReservations.fxml");
    }

    @FXML
    private void showSearchBooksPage() {
        System.out.println("Navigating to Search Books Page.");
        loadFXMLIntoContentPane("/com/library/librarymanagementsystemui/PatronSearch.fxml");
    }

    @FXML
    private void openCreatePatronPage() {
        System.out.println("Navigating Create Patron Page.");
        loadFXMLIntoContentPane("/com/library/librarymanagementsystemui/CreatePatron.fxml");
    }

    @FXML
    private void showAddBookPage() {
        System.out.println("Navigating to Add Book Page.");
        loadFXMLIntoContentPane("/com/library/librarymanagementsystemui/AddBook.fxml");
    }

    @FXML
    private void showManageBooksPage() {
        System.out.println("Navigating to Manage Books Page.");
        loadFXMLIntoContentPane("/com/library/librarymanagementsystemui/ManageBooksPage.fxml");
    }

    @FXML
    private void showTransactionsPage() {
        System.out.println("Navigating to Transactions Page.");
        loadFXMLIntoContentPane("/com/library/librarymanagementsystemui/PatronTransactions.fxml");
    }

    @FXML
    private void showManagePatronsPage() {
        System.out.println("Navigating to Manage Patrons Page.");
        loadFXMLIntoContentPane("/com/library/librarymanagementsystemui/ManagePatrons.fxml");
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM Patrons WHERE Email = ? AND Password = ?")) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Login successful!");

                loggedInEmail = rs.getString("Email");
                loggedInPassword = rs.getString("Password");
                int patronID = rs.getInt("PatronID");
                boolean isLibrarian = rs.getInt("isLibrarian") == 1;

                SessionManager.setCurrentPatronID(patronID);
                SessionManager.setCurrentPatronEmail(loggedInEmail);
                SessionManager.setCurrentPatronPassword(loggedInPassword);

                // Replace login with logout
                loginButton.setVisible(false);
                loginButton.setManaged(false);
                logoutButton.setVisible(true);
                logoutButton.setManaged(true);

                // Show buttons based on user type
                if (isLibrarian) {
                    enableLibrarianButtons();
                } else {
                    enablePatronButtons();
                }
            } else {
                System.out.println("Invalid email or password.");
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    private void enableLibrarianButtons() {
        // Show librarian-specific buttons
        addBookButton.setVisible(true);
        addBookButton.setManaged(true);

        manageBooksButton.setVisible(true);
        manageBooksButton.setManaged(true);

        viewTransactionsButton.setVisible(true);
        viewTransactionsButton.setManaged(true);

        managePatronsButton.setVisible(true);
        managePatronsButton.setManaged(true);

        // Hide patron-specific buttons
        searchBooksButton.setVisible(false);
        searchBooksButton.setManaged(false);

        borrowedBooksButton.setVisible(false);
        borrowedBooksButton.setManaged(false);

        updateAccountButton.setVisible(false);
        updateAccountButton.setManaged(false);

        wishlistButton.setVisible(false);
        wishlistButton.setManaged(false);

        reservationsButton.setVisible(false);
        reservationsButton.setManaged(false);
    }

    private void enablePatronButtons() {
        // Show patron-specific buttons
        searchBooksButton.setVisible(true);
        searchBooksButton.setManaged(true);

        borrowedBooksButton.setVisible(true);
        borrowedBooksButton.setManaged(true);

        updateAccountButton.setVisible(true);
        updateAccountButton.setManaged(true);

        wishlistButton.setVisible(true);
        wishlistButton.setManaged(true);

        reservationsButton.setVisible(true);
        reservationsButton.setManaged(true);

        // Hide librarian-specific buttons
        addBookButton.setVisible(false);
        addBookButton.setManaged(false);

        manageBooksButton.setVisible(false);
        manageBooksButton.setManaged(false);

        viewTransactionsButton.setVisible(false);
        viewTransactionsButton.setManaged(false);

        managePatronsButton.setVisible(false);
        managePatronsButton.setManaged(false);
    }

    @FXML
    private void handleLogout() {
        System.out.println("Logging out.");

        // Clear session and reset buttons
        SessionManager.clearSession();
        resetButtons();

        // Clear login fields
        emailField.clear();
        passwordField.clear();

        // Replace logout with login
        logoutButton.setVisible(false);
        logoutButton.setManaged(false);
        loginButton.setVisible(true);
        loginButton.setManaged(true);

        // Refresh UI to home page
        showHomePage();
    }

    private void resetButtons() {
        // Hide all buttons (except Home)
        addBookButton.setVisible(false);
        manageBooksButton.setVisible(false);
        viewTransactionsButton.setVisible(false);
        managePatronsButton.setVisible(false);
        searchBooksButton.setVisible(false);
        borrowedBooksButton.setVisible(false);
        updateAccountButton.setVisible(false);
        wishlistButton.setVisible(false);
        reservationsButton.setVisible(false);
    }
}