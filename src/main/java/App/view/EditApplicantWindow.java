package App.view;

import App.controller.command.Param;
import App.model.entity.Applicant;
import App.view.controls.AddApplicantPane;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class EditApplicantWindow {
    private Stage stage;
    private Applicant applicant;

    public EditApplicantWindow(Applicant applicant){
        this.applicant = applicant;
        load();
    }

    void load() {
            AddApplicantPane root = new AddApplicantPane(applicant);
            root.setOnClose(EventHandler -> close());
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(MainScreen.class.getResource("style/style.css").toExternalForm());
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Edit applicant");
            stage.setResizable(false);

            stage.initOwner(Main.getCurrentWindow().getStage());
            stage.initModality(Modality.WINDOW_MODAL);

            stage.showAndWait();
    }

    private void close(){
        stage.close();
    }
}
