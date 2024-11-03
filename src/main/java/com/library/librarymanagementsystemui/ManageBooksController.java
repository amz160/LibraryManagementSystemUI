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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class ManageBooksController {

    @FXML
    private TableView<Book> booksTable;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, Void> actionColumn;

    private ObservableList<Book> booksList;

    @FXML
    public void initialize() {
        // Columns in the table
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        // Load books into the table
        BookDAO bookDAO = new BookDAO();
        booksList = FXCollections.observableArrayList(bookDAO.getAllBooks());
        booksTable.setItems(booksList);

        // Adds the Delete button to each row
        addDeleteButtonToTable();
    }

    private void addDeleteButtonToTable() {
        Callback<TableColumn<Book, Void>, TableCell<Book, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Book, Void> call(final TableColumn<Book, Void> param) {
                return new TableCell<>() {
                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.setOnAction(event -> {
                            Book book = getTableView().getItems().get(getIndex());
                            handleDeleteBook(book);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox hbox = new HBox(deleteButton);
                            hbox.setSpacing(5);
                            setGraphic(hbox);
                        }
                    }
                };
            }
        };

        actionColumn.setCellFactory(cellFactory);
    }

    private void handleDeleteBook(Book book) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the book: " + book.getTitle() + "?", ButtonType.YES, ButtonType.NO);
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                BookDAO bookDAO = new BookDAO();
                if (bookDAO.deleteBook(book.getBookID())) {
                    booksList.remove(book);
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to delete book: " + book.getTitle(), ButtonType.OK);
                    errorAlert.showAndWait();
                }
            }
        });
    }

    @FXML
    private void handleCancel() {
        booksTable.getScene().getWindow().hide();
    }
}
