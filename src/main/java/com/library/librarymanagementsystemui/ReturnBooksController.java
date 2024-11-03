package com.library.librarymanagementsystemui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.util.List;

public class ReturnBooksController {

    @FXML
    private TableView<Book> borrowedBooksTable;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> dueDateColumn;

    @FXML
    private TableColumn<Book, Void> actionColumn;

    private ObservableList<Book> borrowedBooksList;

    @FXML
    public void initialize() {
        System.out.println("Initializing ReturnBooksController");

        // Columns in the table
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        dueDateColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getDueDate() != null) {
                return new SimpleStringProperty(cellData.getValue().getDueDate().toString());
            } else {
                return new SimpleStringProperty("No Due Date");
            }
        });

        // Adds the return button to each row in the action column
        addReturnButtonToTable();

        // Loads borrowed books into the table
        loadBorrowedBooks();
    }

    private void loadBorrowedBooks() {
        BookDAO bookDAO = new BookDAO();
        int patronID = SessionManager.getCurrentPatronID(); // Gets the current patron ID from the session

        List<Book> borrowedBooks = bookDAO.getBorrowedBooks(patronID); // Pass the patronID
        borrowedBooksList = FXCollections.observableArrayList(borrowedBooks);
        borrowedBooksTable.setItems(borrowedBooksList);

        System.out.println("Loaded books: " + borrowedBooksList.size());
    }

    private void addReturnButtonToTable() {
        Callback<TableColumn<Book, Void>, TableCell<Book, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Book, Void> call(final TableColumn<Book, Void> param) {
                final TableCell<Book, Void> cell = new TableCell<>() {

                    private final Button returnButton = new Button("Return");

                    {
                        returnButton.setOnAction(event -> {
                            Book book = getTableView().getItems().get(getIndex());
                            System.out.println("Return button clicked for book: " + book.getTitle());
                            handleReturnBook(book);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(returnButton);
                        }
                    }
                };
                return cell;
            }
        };
        actionColumn.setCellFactory(cellFactory);
    }

    private void handleReturnBook(Book book) {
        BookDAO bookDAO = new BookDAO();
        int patronID = SessionManager.getCurrentPatronID();

        if (bookDAO.returnBook(book.getBookID(), patronID)) {
            borrowedBooksList.remove(book);
            borrowedBooksTable.refresh();

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION,
                    "Successfully returned \"" + book.getTitle() + "\".", ButtonType.OK);
            successAlert.showAndWait();
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to return book: " + book.getTitle(), ButtonType.OK);
            errorAlert.showAndWait();
        }
    }

    @FXML
    private void handleCancel() {
        borrowedBooksTable.getScene().getWindow().hide();
    }
}