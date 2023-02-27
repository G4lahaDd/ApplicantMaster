package App.view;

import App.model.entity.Faculty;
import App.model.service.ApplicationDataService;
import App.view.controls.FacultyPane;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;

public class MessageBox{
    public MessageBox(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("style/style.css").toExternalForm());
        //alert.getGraphic().getScene().getStylesheets().add(getClass().getResource("style/style.css").toExternalForm());
        alert.showAndWait();
    }
}
