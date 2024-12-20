package com.library.librarymanagementsystemui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LibraryApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/librarymanagementsystemui/LibraryMainPage.fxml"));
            Parent root = loader.load();
            primaryStage.setTitle("Library Management System");
            primaryStage.setScene(new Scene(root, 1200, 800));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
