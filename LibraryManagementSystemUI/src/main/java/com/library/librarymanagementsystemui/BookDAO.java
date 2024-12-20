package com.library.librarymanagementsystemui;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    // Add a new book to the database
    public boolean addBook(Book book) {
        String sql = "INSERT INTO Books (bookId, title, author, genre, publicationYear, availability) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, book.getBookID());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getGenre());
            statement.setInt(5, book.getPublicationYear());
            statement.setBoolean(6, book.isAvailable());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding book: " + e.getMessage());
            return false;
        }
    }

    // Delete a book by bookId
    public boolean deleteBook(int bookId) {
        String sql = "DELETE FROM Books WHERE bookId = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bookId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting book: " + e.getMessage());
            return false;
        }
    }

    // Retrieve all books
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Books";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                books.add(new Book(resultSet.getInt("bookId"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("genre"),
                        resultSet.getInt("publicationYear"),
                        resultSet.getBoolean("availability")));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving books: " + e.getMessage());
        }
        return books;
    }

    // Borrow a book by bookId and due date
    public boolean borrowBook(int bookID, int patronID, LocalDate dueDate) {
        System.out.println("Borrowing book...");
        System.out.println("BookID: " + bookID + ", PatronID: " + patronID + ", DueDate: " + dueDate);

        String checkExistingTransaction = "SELECT * FROM Transactions WHERE PatronID = ? AND BookID = ? AND Returned = 0";
        String updateBookSql = "UPDATE Books SET availability = 0, dueDate = ? WHERE BookID = ?";
        String insertTransactionSql = "INSERT INTO Transactions (PatronID, BookID, BorrowDate, DueDate, Returned) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            // Check if there is already an active transaction for this book and patron
            try (PreparedStatement checkStmt = connection.prepareStatement(checkExistingTransaction)) {
                checkStmt.setInt(1, patronID);
                checkStmt.setInt(2, bookID);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    System.out.println("Book is already borrowed by the patron.");
                    return false;
                }
            }

            // Continue with borrowing the book
            try (PreparedStatement updateBookStmt = connection.prepareStatement(updateBookSql);
                 PreparedStatement insertTransactionStmt = connection.prepareStatement(insertTransactionSql)) {

                // Update the book availability
                updateBookStmt.setDate(1, java.sql.Date.valueOf(dueDate));
                updateBookStmt.setInt(2, bookID);
                int bookUpdateResult = updateBookStmt.executeUpdate();
                System.out.println("Book availability updated. Rows affected: " + bookUpdateResult);

                // Insert the transaction
                insertTransactionStmt.setInt(1, patronID);
                insertTransactionStmt.setInt(2, bookID);
                insertTransactionStmt.setDate(3, java.sql.Date.valueOf(LocalDate.now())); // Borrow date is today
                insertTransactionStmt.setDate(4, java.sql.Date.valueOf(dueDate));
                insertTransactionStmt.setBoolean(5, false); // Not returned yet
                int transactionResult = insertTransactionStmt.executeUpdate();
                System.out.println("Transaction inserted. Rows affected: " + transactionResult);

                connection.commit();
                System.out.println("Borrow transaction committed successfully.");
                return true;
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Error during borrowing process: " + e.getMessage());
                return false;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
            return false;
        }
    }

    // Return a book by bookID
    public boolean returnBook(int bookID, int patronID) {
        String updateBookSql = "UPDATE Books SET Availability = 1, DueDate = NULL WHERE BookID = ?";
        String updateTransactionSql = "UPDATE Transactions SET Returned = 1 WHERE BookID = ? AND PatronID = ? AND Returned = 0";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement updateBookStmt = connection.prepareStatement(updateBookSql);
                 PreparedStatement updateTransactionStmt = connection.prepareStatement(updateTransactionSql)) {

                // Update the book to show it is available
                updateBookStmt.setInt(1, bookID);
                updateBookStmt.executeUpdate();

                // Update the transaction to mark it as returned
                updateTransactionStmt.setInt(1, bookID);
                updateTransactionStmt.setInt(2, patronID);
                updateTransactionStmt.executeUpdate();

                connection.commit();
                return true;
            } catch (SQLException e) {
                // Rollback in case of failure
                connection.rollback();
                System.out.println("Error returning book (rolling back): " + e.getMessage());
                return false;
            } finally {
                connection.setAutoCommit(true); // Enable auto-commit
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }

    // Retrieve books by keyword (title, author, genre)
    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Books WHERE title LIKE ? OR author LIKE ? OR genre LIKE ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                books.add(new Book(resultSet.getInt("bookId"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("genre"),
                        resultSet.getInt("publicationYear"),
                        resultSet.getBoolean("availability")));
            }
        } catch (SQLException e) {
            System.out.println("Error searching books: " + e.getMessage());
        }
        return books;
    }

    // Get borrowed books for a patron
    public List<Book> getBorrowedBooks(int patronID) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.*, t.DueDate " +
                "FROM Books b " +
                "JOIN Transactions t ON b.BookID = t.BookID " +
                "WHERE t.PatronID = ? AND t.Returned = 0";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, patronID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                books.add(new Book(
                        resultSet.getInt("BookID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Author"),
                        resultSet.getString("Genre"),
                        resultSet.getInt("PublicationYear"),
                        resultSet.getBoolean("Availability"),
                        resultSet.getDate("DueDate") != null ? resultSet.getDate("DueDate").toLocalDate() : null
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving borrowed books: " + e.getMessage());
        }
        return books;
    }

    // Add a book to a patron's wishlist
    public boolean addBookToWishlist(int bookID, int patronID) {
        String sql = "INSERT INTO Wishlist (PatronID, BookID) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, patronID);
            statement.setInt(2, bookID);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding book to wishlist: " + e.getMessage());
            return false;
        }
    }

    // Get wishlist books for a patron
    public List<Book> getWishlistBooks(int patronID) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.* FROM Books b JOIN Wishlist w ON b.BookID = w.BookID WHERE w.PatronID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, patronID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                books.add(new Book(resultSet.getInt("BookID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Author"),
                        resultSet.getString("Genre"),
                        resultSet.getInt("PublicationYear"),
                        resultSet.getBoolean("Availability")));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving wishlist books: " + e.getMessage());
        }
        return books;
    }

    // Remove a book from a patron's wishlist
    public boolean removeBookFromWishlist(int bookID, int patronID) {
        String sql = "DELETE FROM Wishlist WHERE BookID = ? AND PatronID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bookID);
            statement.setInt(2, patronID);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book successfully removed from wishlist. Rows affected: " + rowsAffected);
                return true;
            } else {
                System.out.println("No rows were deleted. Please check if the entry exists for PatronID: " + patronID + ", BookID: " + bookID);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error removing book from wishlist: " + e.getMessage());
            return false;
        }
    }

    // Reserve a book for a patron
    public boolean reserveBook(int patronID, int bookID) {
        System.out.println("Reserving book with PatronID: " + patronID + ", BookID: " + bookID);

        String sql = "INSERT INTO Reservations (PatronID, BookID, AvailableOn) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, patronID);
            statement.setInt(2, bookID);
            statement.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now().plusDays(15))); // Available 15 days later

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error reserving book: " + e.getMessage());
            return false;
        }
    }

    // Retrieve all books with due dates
    public List<Book> getBooksWithDueDates() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Books";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                books.add(new Book(
                        resultSet.getInt("BookID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Author"),
                        resultSet.getString("Genre"),
                        resultSet.getInt("PublicationYear"),
                        resultSet.getBoolean("Availability"),
                        resultSet.getDate("DueDate") != null ? resultSet.getDate("DueDate").toLocalDate() : null
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving books with due dates: " + e.getMessage());
        }
        return books;
    }

    public List<Reservation> getReservationsForPatron(int patronID) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = """
        SELECT b.Title, b.Author, 
               CASE 
                   WHEN b.Availability = 1 THEN 'Available Now' 
                   ELSE DATE_FORMAT(b.DueDate, '%Y-%m-%d') 
               END AS AvailabilityDate
        FROM Reservations r
        JOIN Books b ON r.BookID = b.BookID
        WHERE r.PatronID = ?
    """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, patronID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                reservations.add(new Reservation(
                        resultSet.getString("Title"),
                        resultSet.getString("Author"),
                        resultSet.getString("AvailabilityDate")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving reservations: " + e.getMessage());
        }
        return reservations;
    }
}