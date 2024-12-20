package com.library.librarymanagementsystemui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatronReservationsController {

    @FXML
    private TableView<Reservation> reservationsTable;

    @FXML
    private TableColumn<Reservation, String> titleColumn;

    @FXML
    private TableColumn<Reservation, String> authorColumn;

    @FXML
    private TableColumn<Reservation, String> availabilityColumn;

    @FXML
    private TableColumn<Reservation, Void> deleteColumn;

    private final ObservableList<Reservation> reservationsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        availabilityColumn.setCellValueFactory(cellData -> cellData.getValue().availabilityDateProperty());

        // Delete button
        deleteColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    deleteReservation(reservation.getTitle());
                    reservationsList.remove(reservation);
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

        reservationsTable.setItems(reservationsList);
        loadReservations(); // Load data into the table
    }

    private void loadReservations() {
        reservationsList.clear();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT b.Title, b.Author, r.AvailableOn " +
                             "FROM Reservations r " +
                             "JOIN Books b ON r.BookID = b.BookID " +
                             "WHERE r.PatronID = ?")) {
            statement.setInt(1, SessionManager.getCurrentPatronID()); // Pass the current PatronID
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                // Check if the book is already available
                String availabilityDate = rs.getDate("AvailableOn") != null &&
                        rs.getDate("AvailableOn").toLocalDate().isBefore(java.time.LocalDate.now())
                        ? "Available Now"
                        : rs.getDate("AvailableOn").toString();

                reservationsList.add(new Reservation(
                        rs.getString("Title"),
                        rs.getString("Author"),
                        availabilityDate
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error loading reservations: " + e.getMessage());
        }
    }

    private void deleteReservation(String title) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM Reservations WHERE BookID = (SELECT BookID FROM Books WHERE Title = ?) AND PatronID = ?")) {
            statement.setString(1, title);
            statement.setInt(2, SessionManager.getCurrentPatronID());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Reservation deleted successfully.");
            } else {
                System.out.println("Failed to delete reservation.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting reservation: " + e.getMessage());
        }
    }
}
