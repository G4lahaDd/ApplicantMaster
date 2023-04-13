package App.view;

import App.controller.Controller;
import App.model.entity.Applicant;
import App.view.controls.AddApplicantPane;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditApplicantWindow {
    private Stage stage;
    private Applicant applicant;

    public EditApplicantWindow(Applicant applicant) {
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

        Window window = (Window) Controller
                .getInstance()
                .doReturnCommand("get-current-window");
        Stage InitWindow = window.getStage();
        stage.initModality(Modality.WINDOW_MODAL);

        stage.showAndWait();
    }

    private void close() {
        stage.close();
    }
}
