package com.library.librarymanagementsystemui;

public class Patron {
    private int userId;
    private String name;
    private String email;

    // Constructor
    public Patron(int userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    // Getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Patron{" + "userId=" + userId + ", name='" + name + '\'' + ", email='" + email + '\'' + '}';
    }
}