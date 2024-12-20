package com.library.librarymanagementsystemui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.util.List;

public class WishlistController {

    @FXML
    private TableView<Book> wishlistTable;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> genreColumn;

    @FXML
    private TableColumn<Book, Void> actionColumn;

    private ObservableList<Book> wishlistBooks;

    public void initialize() {
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        genreColumn.setCellValueFactory(cellData -> cellData.getValue().genreProperty());

        addRemoveButtonToTable();

        loadWishlistBooks();
    }

    private void loadWishlistBooks() {
        BookDAO bookDAO = new BookDAO();
        int patronId = SessionManager.getCurrentPatronID();
        List<Book> books = bookDAO.getWishlistBooks(patronId);
        wishlistBooks = FXCollections.observableArrayList(books);
        wishlistTable.setItems(wishlistBooks);
    }

    private void addRemoveButtonToTable() {
        Callback<TableColumn<Book, Void>, TableCell<Book, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Book, Void> call(final TableColumn<Book, Void> param) {
                final TableCell<Book, Void> cell = new TableCell<>() {
                    private final Button removeButton = new Button("Remove");

                    {
                        removeButton.setOnAction(event -> {
                            Book book = getTableView().getItems().get(getIndex());
                            handleRemoveBook(book);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(removeButton);
                        }
                    }
                };
                return cell;
            }
        };

        actionColumn.setCellFactory(cellFactory);
    }

    private void handleRemoveBook(Book book) {
        int patronId = SessionManager.getCurrentPatronID();
        System.out.println("Attempting to remove BookID: " + book.getBookID() + " for PatronID: " + patronId);

        BookDAO bookDAO = new BookDAO();
        if (bookDAO.removeBookFromWishlist(book.getBookID(), patronId)) {
            wishlistBooks.remove(book);
            System.out.println("Successfully removed book from wishlist: " + book.getTitle());
        } else {
            System.out.println("Failed to remove book from wishlist: " + book.getTitle());
        }
    }
}
