package com.library.librarymanagementsystemui;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.sql.Date;

public class Book {

    private int bookID;
    private StringProperty title;
    private StringProperty author;
    private StringProperty genre;
    private IntegerProperty publicationYear;
    private BooleanProperty availability;
    private ObjectProperty<LocalDate> dueDate;

    public Book(int bookID, String title, String author, String genre, int publicationYear, boolean availability, LocalDate dueDate) {
        this.bookID = bookID;
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.genre = new SimpleStringProperty(genre);
        this.publicationYear = new SimpleIntegerProperty(publicationYear);
        this.availability = new SimpleBooleanProperty(availability);
        this.dueDate = new SimpleObjectProperty<>(dueDate);
    }

    public Book(int bookID, String title, String author, String genre, int publicationYear, boolean availability) {
        this(bookID, title, author, genre, publicationYear, availability, null);
    }

    // Getters and Setters
    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public StringProperty authorProperty() {
        return author;
    }

    public String getGenre() {
        return genre.get();
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public int getPublicationYear() {
        return publicationYear.get();
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear.set(publicationYear);
    }

    public IntegerProperty publicationYearProperty() {
        return publicationYear;
    }

    public boolean isAvailable() {
        return availability.get();
    }

    public void setAvailability(boolean availability) {
        this.availability.set(availability);
    }

    public BooleanProperty availabilityProperty() {
        return availability;
    }

    public LocalDate getDueDate() {
        return dueDate.get();
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate.set(dueDate);
    }

    public ObjectProperty<LocalDate> dueDateProperty() {
        return dueDate;
    }

    // Convert java.sql.Date to java.time.LocalDate
    public static LocalDate toLocalDate(Date date) {
        return date != null ? date.toLocalDate() : null;
    }

    // Convert java.time.LocalDate to java.sql.Date
    public static Date toSqlDate(LocalDate localDate) {
        return localDate != null ? Date.valueOf(localDate) : null;
    }
}
