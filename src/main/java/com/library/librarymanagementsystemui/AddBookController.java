package com.library.librarymanagementsystemui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AddBookController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField genreField;

    @FXML
    private TextField yearField;

    @FXML
    public void handleAddBook() {
        String title = titleField.getText();
        String author = authorField.getText();
        String genre = genreField.getText();
        String yearText = yearField.getText();

        // Input validation
        if (title.isEmpty() || author.isEmpty() || genre.isEmpty() || yearText.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR, "All fields must be filled in.");
            alert.show();
            return;
        }

        int year;
        try {
            year = Integer.parseInt(yearText);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR, "Year must be a valid number.");
            alert.show();
            return;
        }

        Book newBook = new Book(0, title, author, genre, year, true);

        // Add the book to the database
        BookDAO bookDAO = new BookDAO();
        if (bookDAO.addBook(newBook)) {
            Alert alert = new Alert(AlertType.INFORMATION, "Book added successfully.");
            alert.show();
        } else {
            Alert alert = new Alert(AlertType.ERROR, "Failed to add book. Please try again.");
            alert.show();
        }
    }
}
