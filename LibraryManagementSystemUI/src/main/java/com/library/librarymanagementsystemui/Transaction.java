package com.library.librarymanagementsystemui;

import javafx.beans.property.*;

public class Transaction {
    private final IntegerProperty patronId;
    private final StringProperty patronName;
    private final IntegerProperty bookId;
    private final StringProperty bookTitle;
    private final StringProperty borrowDate;
    private final StringProperty dueDate;
    private final StringProperty returnDate;

    public Transaction(int patronId, String patronName, int bookId, String bookTitle,
                       String borrowDate, String dueDate, String returnDate) {
        this.patronId = new SimpleIntegerProperty(patronId);
        this.patronName = new SimpleStringProperty(patronName);
        this.bookId = new SimpleIntegerProperty(bookId);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.borrowDate = new SimpleStringProperty(borrowDate);
        this.dueDate = new SimpleStringProperty(dueDate);
        this.returnDate = new SimpleStringProperty(returnDate);
    }

    public int getPatronId() {
        return patronId.get();
    }

    public IntegerProperty patronIdProperty() {
        return patronId;
    }

    public String getPatronName() {
        return patronName.get();
    }

    public StringProperty patronNameProperty() {
        return patronName;
    }

    public int getBookId() {
        return bookId.get();
    }

    public IntegerProperty bookIdProperty() {
        return bookId;
    }

    public String getBookTitle() {
        return bookTitle.get();
    }

    public StringProperty bookTitleProperty() {
        return bookTitle;
    }

    public String getBorrowDate() {
        return borrowDate.get();
    }

    public StringProperty borrowDateProperty() {
        return borrowDate;
    }

    public String getDueDate() {
        return dueDate.get();
    }

    public StringProperty dueDateProperty() {
        return dueDate;
    }

    public String getReturnDate() {
        return returnDate.get();
    }

    public StringProperty returnDateProperty() {
        return returnDate;
    }
}
