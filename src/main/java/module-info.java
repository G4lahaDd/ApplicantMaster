module App{
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    exports App.controller;
    opens App.controller to javafx.fxml;
    exports App.view;
    opens App.view to javafx.fxml;
    exports App.view.controls;
    opens App.view.controls to javafx.fxml;
}
