package com.library.librarymanagementsystemui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Book {

    private int bookID;
    private StringProperty title;
    private StringProperty author;
    private StringProperty genre;
    private int publicationYear;
    private BooleanProperty available;
    private BooleanProperty selected;
    private ObjectProperty<LocalDate> dueDate;

    public Book(int bookID, String title, String author, String genre, int publicationYear, boolean availability) {
        this.bookID = bookID;
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.genre = new SimpleStringProperty(genre);
        this.publicationYear = publicationYear;
        this.available = new SimpleBooleanProperty(availability);
        this.selected = new SimpleBooleanProperty(false);
        this.dueDate = new SimpleObjectProperty<>(null); // Default to null if no due date
    }

    public Book(int bookID, String title, String author, String genre, int publicationYear, boolean availability, LocalDate dueDate) {
        this.bookID = bookID;
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.genre = new SimpleStringProperty(genre);
        this.publicationYear = publicationYear;
        this.available = new SimpleBooleanProperty(availability);
        this.selected = new SimpleBooleanProperty(false);
        this.dueDate = new SimpleObjectProperty<>(dueDate);
    }

    // Getter and Setter for Book ID
    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    // Title Property Getter and Setter
    public StringProperty titleProperty() {
        return title;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    // Author Property Getter and Setter
    public StringProperty authorProperty() {
        return author;
    }

    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    // Genre Property Getter and Setter
    public StringProperty genreProperty() {
        return genre;
    }

    public String getGenre() {
        return genre.get();
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    // Publication Year Getter and Setter
    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    // Available Property Getter and Setter
    public BooleanProperty availableProperty() {
        return available;
    }

    public boolean isAvailable() {
        return available.get();
    }

    public void setAvailable(boolean availability) {
        this.available.set(availability);
    }

    // Selected Property Getter and Setter
    public BooleanProperty selectedProperty() {
        return selected;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    // Due Date Property Getter and Setter
    public ObjectProperty<LocalDate> dueDateProperty() {
        return dueDate;
    }

    public LocalDate getDueDate() {
        return dueDate.get();
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate.set(dueDate);
    }
}
