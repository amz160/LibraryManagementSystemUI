package com.library.librarymanagementsystemui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.List;

public class PatronSearchController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Book> searchResultsTable;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> genreColumn;

    @FXML
    private TableColumn<Book, String> dueDateColumn;

    @FXML
    private TableColumn<Book, Void> actionColumn;

    @FXML
    private TableColumn<Book, Void> reserveColumn;

    private final BookDAO bookDAO = new BookDAO();
    private final ObservableList<Book> books = FXCollections.observableArrayList();

    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        // Due date aka availability
        dueDateColumn.setCellValueFactory(book -> {
            LocalDate dueDate = book.getValue().getDueDate();
            if (dueDate != null) {
                return new SimpleStringProperty(dueDate.toString());
            } else if (book.getValue().isAvailable()) {
                return new SimpleStringProperty("Available");
            } else {
                return new SimpleStringProperty("Unavailable");
            }
        });

        addActionButtonsToTable();
        addReserveButtonToTable();

        // Load all books initially
        loadBooks();
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().trim();
        if (!keyword.isEmpty()) {
            List<Book> filteredBooks = bookDAO.searchBooks(keyword);
            books.setAll(filteredBooks);
        } else {
            loadBooks();
        }
    }

    private void loadBooks() {
        books.setAll(bookDAO.getAllBooks());
        searchResultsTable.setItems(books);
    }

    private void addActionButtonsToTable() {
        Callback<TableColumn<Book, Void>, TableCell<Book, Void>> cellFactory = param -> new TableCell<>() {
            private final Button borrowButton = new Button("Borrow");
            private final Button wishlistButton = new Button("Add to Wishlist");

            {
                // Borrow button
                borrowButton.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    handleBorrowAction(book);
                });

                // Wishlist button
                wishlistButton.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    handleWishlistAction(book);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox actionButtons = new HBox(10, borrowButton, wishlistButton);
                    setGraphic(actionButtons);
                }
            }
        };

        actionColumn.setCellFactory(cellFactory);
    }

    private void addReserveButtonToTable() {
        reserveColumn.setCellFactory(param -> new TableCell<>() {
            private final Button reserveButton = new Button("Reserve");

            {
                reserveButton.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    handleReserveAction(book);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(reserveButton);
                }
            }
        });
    }

    private void handleBorrowAction(Book book) {
        Alert alert;
        boolean success = bookDAO.borrowBook(book.getBookID(), SessionManager.getCurrentPatronID(), LocalDate.now().plusDays(14)); // Typical loan days
        if (success) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Book borrowed successfully!");
        } else {
            alert = new Alert(Alert.AlertType.ERROR, "Unable to borrow the book. Please try again.");
        }
        alert.showAndWait();
    }

    private void handleWishlistAction(Book book) {
        Alert alert;
        boolean success = bookDAO.addBookToWishlist(book.getBookID(), SessionManager.getCurrentPatronID());
        if (success) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Book added to wishlist successfully!");
        } else {
            alert = new Alert(Alert.AlertType.ERROR, "Unable to add the book to wishlist.");
        }
        alert.showAndWait();
    }

    private void handleReserveAction(Book book) {
        Alert alert;
        boolean success = bookDAO.reserveBook(SessionManager.getCurrentPatronID(), book.getBookID());
        if (success) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Book reserved successfully!");
        } else {
            alert = new Alert(Alert.AlertType.ERROR, "Unable to reserve the book. Please try again.");
        }
        alert.showAndWait();
    }
}
