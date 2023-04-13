module App{
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.poi.poi;
    requires org.apache.logging.log4j;
    requires mysql.connector.j;


    exports App.controller;
    opens App.controller to javafx.fxml;
    exports App.view;
    opens App.view to javafx.fxml;
    exports App.view.controls;
    opens App.view.controls to javafx.fxml;
    exports App.model.entity;
    opens App.model.entity to javafx.fxml;
    exports App.model.entity.groups;
    opens App.model.entity.groups to javafx.fxml;
}
