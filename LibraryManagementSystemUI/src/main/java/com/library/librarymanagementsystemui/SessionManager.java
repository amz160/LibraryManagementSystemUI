package com.library.librarymanagementsystemui;

public class SessionManager {

    private static String currentPatronEmail;
    private static String currentPatronPassword;
    private static int currentPatronID;

    // Getter for current patron email
    public static String getCurrentPatronEmail() {
        return currentPatronEmail;
    }

    // Setter for current patron email
    public static void setCurrentPatronEmail(String email) {
        currentPatronEmail = email;
    }

    // Getter for current patron password
    public static String getCurrentPatronPassword() {
        return currentPatronPassword;
    }

    // Setter for current patron password
    public static void setCurrentPatronPassword(String password) {
        currentPatronPassword = password;
    }

    // Getter for current patron ID
    public static int getCurrentPatronID() {
        return currentPatronID;
    }

    // Setter for current patron ID
    public static void setCurrentPatronID(int patronID) {
        currentPatronID = patronID;
    }

    // Clear the session
    public static void clearSession() {
        currentPatronEmail = null;
        currentPatronPassword = null;
        currentPatronID = 0;
    }
}
