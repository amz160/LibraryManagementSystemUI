package com.library.librarymanagementsystemui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Patron {

    private final SimpleIntegerProperty patronID;
    private final SimpleStringProperty name;
    private final SimpleStringProperty email;
    private final SimpleStringProperty password;
    private final SimpleBooleanProperty isLibrarian;

    public Patron(int patronID, String name, String email, String password, boolean isLibrarian) {
        this.patronID = new SimpleIntegerProperty(patronID);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.isLibrarian = new SimpleBooleanProperty(isLibrarian);
    }

    public int getPatronID() {
        return patronID.get();
    }

    public String getName() {
        return name.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getPassword() {
        return password.get();
    }

    public boolean getIsLibrarian() {
        return isLibrarian.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public void setIsLibrarian(boolean isLibrarian) {
        this.isLibrarian.set(isLibrarian);
    }
}
