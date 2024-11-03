package com.library.librarymanagementsystemui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox; // Add this import statement
import javafx.util.Callback;

import java.time.LocalDate; // Add this import statement
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
    private TableColumn<Book, Void> actionColumn;

    private ObservableList<Book> searchResultsList;

    @FXML
    public void initialize() {
        // Columns in the table
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        genreColumn.setCellValueFactory(cellData -> cellData.getValue().genreProperty());

        // Adds the borrow and add-to-wishlist buttons to each row in the action column
        addBorrowAndWishlistButtonsToTable();
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a keyword to search.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Perform search
        BookDAO bookDAO = new BookDAO();
        List<Book> foundBooks = bookDAO.searchBooks(keyword);
        searchResultsList = FXCollections.observableArrayList(foundBooks);
        searchResultsTable.setItems(searchResultsList);
    }

    private void addBorrowAndWishlistButtonsToTable() {
        Callback<TableColumn<Book, Void>, TableCell<Book, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Book, Void> call(final TableColumn<Book, Void> param) {
                final TableCell<Book, Void> cell = new TableCell<>() {

                    private final Button borrowButton = new Button("Borrow");
                    private final Button wishlistButton = new Button("Add to Wishlist");

                    {
                        // Handle borrow button click
                        borrowButton.setOnAction(event -> {
                            Book book = getTableView().getItems().get(getIndex());
                            if (book.isAvailable()) {
                                handleBorrowBook(book);
                            } else {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION, "This book is currently unavailable.", ButtonType.OK);
                                alert.showAndWait();
                            }
                        });

                        // Handle wishlist button click
                        wishlistButton.setOnAction(event -> {
                            Book book = getTableView().getItems().get(getIndex());
                            BookDAO bookDAO = new BookDAO();
                            int patronID = SessionManager.getCurrentPatronID();
                            if (bookDAO.addBookToWishlist(book.getBookID(), patronID)) {
                                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Book added to wishlist successfully.", ButtonType.OK);
                                successAlert.showAndWait();
                            } else {
                                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to add book to wishlist.", ButtonType.OK);
                                errorAlert.showAndWait();
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox hbox = new HBox(borrowButton, wishlistButton);
                            hbox.setSpacing(5);
                            setGraphic(hbox);
                        }
                    }
                };
                return cell;
            }
        };
        actionColumn.setCellFactory(cellFactory);
    }

    private void handleBorrowBook(Book book) {
        int patronID = SessionManager.getCurrentPatronID();
        LocalDate dueDate = LocalDate.now().plusWeeks(2); // 2 weeks from day borrowed
        BookDAO bookDAO = new BookDAO();

        if (bookDAO.borrowBook(book.getBookID(), patronID, dueDate)) {
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION,
                    "Successfully borrowed \"" + book.getTitle() + "\". Due date: " + dueDate, ButtonType.OK);
            successAlert.showAndWait();
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                    "Failed to borrow book: " + book.getTitle(), ButtonType.OK);
            errorAlert.showAndWait();
        }
    }
}
