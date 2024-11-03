module com.library.librarymanagementsystemui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.library.librarymanagementsystemui to javafx.fxml;
    exports com.library.librarymanagementsystemui;
}