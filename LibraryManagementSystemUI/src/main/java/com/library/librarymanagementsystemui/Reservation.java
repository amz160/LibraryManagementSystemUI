package com.library.librarymanagementsystemui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Reservation {

    private final StringProperty title;
    private final StringProperty author;
    private final StringProperty availabilityDate;

    public Reservation(String title, String author, String availabilityDate) {
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.availabilityDate = new SimpleStringProperty(availabilityDate);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getAuthor() {
        return author.get();
    }

    public StringProperty authorProperty() {
        return author;
    }

    public String getAvailabilityDate() {
        return availabilityDate.get();
    }

    public StringProperty availabilityDateProperty() {
        return availabilityDate;
    }
}