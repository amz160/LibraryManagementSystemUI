package com.library.librarymanagementsystemui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagePatronsController {

    @FXML
    private TableView<Patron> patronsTable;

    @FXML
    private TableColumn<Patron, Integer> patronIdColumn;

    @FXML
    private TableColumn<Patron, String> nameColumn;

    @FXML
    private TableColumn<Patron, String> emailColumn;

    @FXML
    private TableColumn<Patron, String> passwordColumn;

    @FXML
    private TableColumn<Patron, String> isLibrarianColumn;

    @FXML
    private TableColumn<Patron, Void> actionColumn;

    private final ObservableList<Patron> patronsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        patronIdColumn.setCellValueFactory(new PropertyValueFactory<>("patronID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        isLibrarianColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getIsLibrarian() ? "Yes" : "No")
        );

        // Edit and Delete buttons
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(event -> {
                    Patron patron = getTableView().getItems().get(getIndex());
                    editPatron(patron);
                });

                deleteButton.setOnAction(event -> {
                    Patron patron = getTableView().getItems().get(getIndex());
                    deletePatron(patron);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, editButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });

        patronsTable.setItems(patronsList);
        loadPatrons();
    }

    private void loadPatrons() {
        patronsList.clear();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Patrons")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                patronsList.add(new Patron(
                        resultSet.getInt("PatronID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Email"),
                        resultSet.getString("Password"),
                        resultSet.getInt("isLibrarian") == 1 // Convert int to boolean
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error loading patrons: " + e.getMessage());
        }
    }

    private void editPatron(Patron patron) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Patron");
        dialog.setHeaderText("Editing Patron: " + patron.getName());

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        TextField nameField = new TextField(patron.getName());
        TextField emailField = new TextField(patron.getEmail());
        PasswordField passwordField = new PasswordField();
        passwordField.setText(patron.getPassword());
        CheckBox isLibrarianCheckBox = new CheckBox();
        isLibrarianCheckBox.setSelected(patron.getIsLibrarian());

        gridPane.add(new Label("Name:"), 0, 0);
        gridPane.add(nameField, 1, 0);
        gridPane.add(new Label("Email:"), 0, 1);
        gridPane.add(emailField, 1, 1);
        gridPane.add(new Label("Password:"), 0, 2);
        gridPane.add(passwordField, 1, 2);
        gridPane.add(new Label("Librarian:"), 0, 3);
        gridPane.add(isLibrarianCheckBox, 1, 3);

        dialog.getDialogPane().setContent(gridPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try (Connection connection = DatabaseConnection.getConnection();
                     PreparedStatement statement = connection.prepareStatement(
                             "UPDATE Patrons SET Name = ?, Email = ?, Password = ?, isLibrarian = ? WHERE PatronID = ?")) {
                    statement.setString(1, nameField.getText());
                    statement.setString(2, emailField.getText());
                    statement.setString(3, passwordField.getText());
                    statement.setInt(4, isLibrarianCheckBox.isSelected() ? 1 : 0); // Convert boolean to int
                    statement.setInt(5, patron.getPatronID());

                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        patron.setName(nameField.getText());
                        patron.setEmail(emailField.getText());
                        patron.setPassword(passwordField.getText());
                        patron.setIsLibrarian(isLibrarianCheckBox.isSelected());
                        patronsTable.refresh();
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Patron updated successfully.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to update patron.");
                    }
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Error updating patron: " + e.getMessage());
                }
            }
        });
    }

    private void deletePatron(Patron patron) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Patron");
        confirmAlert.setHeaderText("Are you sure you want to delete this patron?");
        confirmAlert.setContentText("This action cannot be undone.");

        confirmAlert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try (Connection connection = DatabaseConnection.getConnection();
                     PreparedStatement statement = connection.prepareStatement(
                             "DELETE FROM Patrons WHERE PatronID = ?")) {
                    statement.setInt(1, patron.getPatronID());

                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        patronsList.remove(patron);
                        patronsTable.refresh();
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Patron deleted successfully.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete patron.");
                    }
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Error deleting patron: " + e.getMessage());
                }
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
