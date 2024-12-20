package com.library.librarymanagementsystemui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatronTransactionsController {

    @FXML
    private TableView<Transaction> transactionsTable;

    @FXML
    private TableColumn<Transaction, Integer> patronIdColumn;

    @FXML
    private TableColumn<Transaction, String> patronNameColumn;

    @FXML
    private TableColumn<Transaction, Integer> bookIdColumn;

    @FXML
    private TableColumn<Transaction, String> bookTitleColumn;

    @FXML
    private TableColumn<Transaction, String> borrowDateColumn;

    @FXML
    private TableColumn<Transaction, String> dueDateColumn;

    @FXML
    private TableColumn<Transaction, String> returnDateColumn;

    private final ObservableList<Transaction> transactionList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        patronIdColumn.setCellValueFactory(new PropertyValueFactory<>("patronId"));
        patronNameColumn.setCellValueFactory(new PropertyValueFactory<>("patronName"));
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        loadTransactions();
    }

    private void loadTransactions() {
        transactionList.clear();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT t.PatronID, p.Name AS PatronName, t.BookID, b.Title AS BookTitle, " +
                             "t.BorrowDate, t.DueDate, t.Returned " +
                             "FROM Transactions t " +
                             "JOIN Patrons p ON t.PatronID = p.PatronID " +
                             "JOIN Books b ON t.BookID = b.BookID")) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String returnDate = rs.getBoolean("Returned") ? rs.getString("DueDate") : "Not Returned";
                transactionList.add(new Transaction(
                        rs.getInt("PatronID"),
                        rs.getString("PatronName"),
                        rs.getInt("BookID"),
                        rs.getString("BookTitle"),
                        rs.getString("BorrowDate"),
                        rs.getString("DueDate"),
                        returnDate
                ));
            }
            transactionsTable.setItems(transactionList);
        } catch (SQLException e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
    }
}
