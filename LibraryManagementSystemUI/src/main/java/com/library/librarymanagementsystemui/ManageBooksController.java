package com.library.librarymanagementsystemui;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManageBooksController {

    @FXML
    private TableView<Book> booksTable;

    @FXML
    private TableColumn<Book, Integer> idColumn;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> genreColumn;

    @FXML
    private TableColumn<Book, Integer> yearColumn;

    @FXML
    private TableColumn<Book, String> availabilityColumn;

    @FXML
    private TableColumn<Book, Void> editColumn;

    @FXML
    private TableColumn<Book, Void> deleteColumn;

    private final ObservableList<Book> booksList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));

        // Changes availability
        availabilityColumn.setCellValueFactory(cellData -> {
            boolean isAvailable = cellData.getValue().isAvailable();
            return new ReadOnlyStringWrapper(isAvailable ? "Available" : "Unavailable");
        });

        // Edit button
        editColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    showEditDialog(book);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });

        // Delete button
        deleteColumn.setCellFactory(column -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    deleteBook(book.getBookID());
                    booksList.remove(book);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        booksTable.setItems(booksList);
        loadBooks();
    }

    private void loadBooks() {
        booksList.clear();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Books")) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                booksList.add(new Book(
                        rs.getInt("BookID"),
                        rs.getString("Title"),
                        rs.getString("Author"),
                        rs.getString("Genre"),
                        rs.getInt("PublicationYear"),
                        rs.getBoolean("Availability")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
    }

    private void showEditDialog(Book book) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Book");
        dialog.setHeaderText("Editing Book: " + book.getTitle());

        TextField titleField = new TextField(book.getTitle());
        TextField authorField = new TextField(book.getAuthor());
        TextField genreField = new TextField(book.getGenre());
        TextField yearField = new TextField(String.valueOf(book.getPublicationYear()));

        HBox content = new HBox(10, new Label("Title:"), titleField, new Label("Author:"), authorField, new Label("Genre:"), genreField, new Label("Year:"), yearField);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    int year = Integer.parseInt(yearField.getText());
                    book.setTitle(titleField.getText());
                    book.setAuthor(authorField.getText());
                    book.setGenre(genreField.getText());
                    book.setPublicationYear(year);
                    updateBook(book);
                } catch (NumberFormatException e) {
                    showErrorAlert("Invalid Input", "Please enter a valid year.");
                }
            }
        });
    }

    private void updateBook(Book book) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Books SET Title = ?, Author = ?, Genre = ?, PublicationYear = ? WHERE BookID = ?")) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getGenre());
            statement.setInt(4, book.getPublicationYear());
            statement.setInt(5, book.getBookID());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                showConfirmationAlert("Success", "Book updated successfully.");
                loadBooks(); // Refresh the table
            } else {
                showErrorAlert("Failure", "Failed to update book.");
            }
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Error updating book: " + e.getMessage());
        }
    }

    private void deleteBook(int bookID) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM Books WHERE BookID = ?")) {
            statement.setInt(1, bookID);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                showConfirmationAlert("Success", "Book deleted successfully.");
            } else {
                showErrorAlert("Failure", "Failed to delete book.");
            }
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Error deleting book: " + e.getMessage());
        }
    }

    private void showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
